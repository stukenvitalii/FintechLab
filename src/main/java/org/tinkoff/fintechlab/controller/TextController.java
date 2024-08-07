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

/**
 * TextController is a Spring MVC controller responsible for handling requests related to text translation.
 * It manages the translation process, logs relevant information, handles errors, and communicates with the client and other services.
 */
@Controller
@AllArgsConstructor
public class TextController {

    /** Dependency that handles the translation logic */
    private final TranslateController translateController;

    /** Logger instance for logging information */
    private static final Logger logger = LoggerFactory.getLogger(TextController.class.getName());

    /** Service to handle request-related operations */
    private final RequestService requestService;

    /**
     * Handles the root URL and returns the home page view.
     *
     * @param m the model to hold attributes for the view
     * @return the name of the view to render
     */
    @RequestMapping("/")
    public String home(Model m) {
        m.addAttribute("languages", LanguageCode.values());
        return "index";
    }

    /**
     * Handles the /translated URL for translating text.
     *
     * @param sourceLanguage   the source language code
     * @param targetLanguage   the target language code
     * @param text             the text to be translated
     * @param model            the model to hold attributes for the view
     * @param request          the HTTP request
     * @return the name of the view to render
     * @throws UnknownHostException if the client's IP address cannot be determined
     * @throws ExecutionException   if the translation task fails
     * @throws InterruptedException if the translation task is interrupted
     */
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

    /**
     * Translates text using multithreading to handle multiple words concurrently.
     *
     * @param sourceLanguage the source language code
     * @param targetLanguage the target language code
     * @param text           the text to be translated
     * @return the translated text
     * @throws InterruptedException if the translation task is interrupted
     * @throws ExecutionException   if the translation task fails
     */
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

    /**
     * Handles HttpClientErrorException.
     *
     * @param exception the exception to handle
     * @return the redirection URL for the error page
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientError(HttpClientErrorException exception) {
        if (exception.getStatusCode().value() == 400) {
            logger.info("http 400 текст не может быть пустым");
        }
        if (exception.getStatusCode().value() == 401) {
            logger.info("http 401 не авторизирован");
        }
        return "redirect:/error";
    }

    /**
     * Handles HttpServerErrorException.
     *
     * @param exception the exception to handle
     * @return the redirection URL for the error page
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public String handleServerError(HttpServerErrorException exception) {
        logger.info(exception.getMessage());
        return "redirect:/error";
    }

    /**
     * Retrieves the client's IP address from the request.
     *
     * @param request the HTTP request
     * @return the client's IP address
     */
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

    /**
     * Converts a list of Word objects to a single string.
     *
     * @param arrayList the list of Word objects
     * @return the concatenated string
     */
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

    /**
     * Inner class representing a translated word with its index.
     */
    @Getter
    @AllArgsConstructor
    public static class Word {
        private String translatedWord;
        private Integer idx;
    }

}
