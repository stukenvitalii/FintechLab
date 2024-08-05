package org.tinkoff.fintechlab.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tinkoff.fintechlab.configuration.LanguageCode;
import org.tinkoff.fintechlab.dto.Request;
import org.tinkoff.fintechlab.exception.ClientException;
import org.tinkoff.fintechlab.service.RequestService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.concurrent.*;

@Controller
@AllArgsConstructor
public class TextController {
    private final TranslateController translateController;
    private static final Logger logger = LoggerFactory.getLogger(TextController.class.getName());
    private final RequestService requestService;

    @RequestMapping("/")
    public String home(Model m) {
        m.addAttribute("languages", LanguageCode.values());
        return "index";
    }

    @GetMapping("/translated")
    public String add(
            @RequestParam("sourceLanguage") String sourceLanguage,
            @RequestParam("targetLanguage") String targetLanguage,
            @RequestParam("textForTranslation") String text,
            Model model,
            HttpServletRequest request
    ) throws UnknownHostException {
        model.addAttribute("languages", LanguageCode.values());
        InetAddress clientIp = getClientIp(request);

        if (clientIp != null) {
            logger.info("IP-адрес клиента: {}", clientIp.getHostAddress());
        } else {
            logger.warn("Не удалось определить IP-адрес клиента.");
        }

        logger.info("Исходный язык: {}", sourceLanguage);
        logger.info("Язык перевода: {}", targetLanguage);
        logger.info("Текст для перевода: {}", text);

        String translatedText = "";
        try {
            translatedText = getTranslatedTextWithMultithreading(sourceLanguage, targetLanguage, text);
        } catch (ClientException e) {
            logger.error("Ошибка при переводе текста", e);
            model.addAttribute("errorMessage", "Произошла ошибка при переводе текста. Пожалуйста, попробуйте позже.");
        } catch (RuntimeException e) {
            logger.error("Ошибка при парсинге ответа сервера");
            model.addAttribute("errorMessage", "Произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.");
        } catch (Exception e) {
            logger.error("Общая ошибка", e);
            model.addAttribute("errorMessage", "Произошла непредвиденная ошибка. Пожалуйста, попробуйте позже.");
        }

        requestService.add(new Request(clientIp, text, translatedText));
        logger.info("Saved to DB");

        model.addAttribute("sourceText", text);
        model.addAttribute("translatedText", translatedText);
        return "index";
    }


    private String getTranslatedTextWithMultithreading(String sourceLanguage, String targetLanguage, String text) throws InterruptedException, ExecutionException, ClientException {
        CopyOnWriteArrayList<Word> ans = new CopyOnWriteArrayList<>();
        String[] words = text.split("\\s+");

        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {
            int index = 0;
            for (String word : words) {
                Future<String> completableFuture = executor.submit(() -> translateController.translate(sourceLanguage, targetLanguage, word));
                ans.add(new Word(completableFuture.get(), index));
                index++;
            }

            executor.shutdown();
            ans.sort(Comparator.comparingInt(Word::getIdx));
        }
        return arrayListToString(ans);
    }

    private InetAddress getClientIp(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-Forwarded-For");
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(remoteAddr);
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }

        return inetAddress;
    }

    private String arrayListToString(CopyOnWriteArrayList<Word> arrayList) {
        StringBuilder sb = new StringBuilder();
        for (Word word : arrayList) {
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append(word.getTranslatedWord());
        }
        return sb.toString();
    }

    @Getter
    @AllArgsConstructor
    public static class Word {
        private String translatedWord;
        private Integer idx;
    }
}