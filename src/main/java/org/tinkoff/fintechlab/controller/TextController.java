package org.tinkoff.fintechlab.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.tinkoff.fintechlab.configuration.LanguageCode;
import org.tinkoff.fintechlab.dto.Request;
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
    ) throws UnknownHostException, ExecutionException, InterruptedException {
        model.addAttribute("languages", LanguageCode.values());

        if (sourceLanguage.equals(targetLanguage)) {

            logger.info("Исходный и целевой языки не могут быть одинаковыми");
            return "redirect:/error";
        }

        InetAddress clientIp = getClientIp(request);

        if (clientIp != null) {
            logger.info("IP-адрес клиента: {}", clientIp.getHostAddress());
        } else {
            logger.warn("Не удалось определить IP-адрес клиента.");
        }

        logger.info("Исходный язык: {}", sourceLanguage);
        logger.info("Язык перевода: {}", targetLanguage);
        logger.info("Текст для перевода: {}", text);

        String translatedText = getTranslatedTextWithMultithreading(sourceLanguage, targetLanguage, text);

        requestService.add(new Request(clientIp, text, translatedText));
        logger.info("Saved to DB");

        model.addAttribute("sourceText", text);
        model.addAttribute("translatedText", translatedText);
        return "index";
    }


    private String getTranslatedTextWithMultithreading(String sourceLanguage, String targetLanguage, String text) throws InterruptedException, ExecutionException {
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

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientError(
            HttpClientErrorException
                    exception
    ) {

        if (exception.getStatusCode().value() == 400) {
            logger.info("http 400 текст не может быть пустым");
        }
        if (exception.getStatusCode().value() == 401) {
            logger.info("http 401 не авторизирован");
        }
        return "redirect:/error";
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public String handleServerError(HttpServerErrorException exception) {
        logger.info(exception.getMessage());

        return "redirect:/error";
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