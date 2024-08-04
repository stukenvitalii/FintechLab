package org.tinkoff.fintechlab.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tinkoff.fintechlab.configuration.LanguageCode;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class TextController {
    private final TranslateController translateController;

    private static final Logger logger = LoggerFactory.getLogger(TextController.class.getName());


    public TextController(TranslateController translateController) {
        this.translateController = translateController;
    }

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
    ) throws JSONException {

        InetAddress clientIp = getClientIp(request);

        if (clientIp != null) {
            logger.info("IP-адрес клиента: {}", clientIp.getHostAddress());
        } else {
            logger.warn("Не удалось определить IP-адрес клиента.");
        }

        logger.info("Исходный язык: {}", sourceLanguage);
        logger.info("Язык перевода: {}", targetLanguage);
        logger.info("Текст для перевода: {}", text);

        String translatedText = translateController.translate(sourceLanguage, targetLanguage, text);

        model.addAttribute("sourceText", text);
        model.addAttribute("translatedText", translatedText);
        return "index";
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
}