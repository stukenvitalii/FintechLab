package org.tinkoff.fintechlab.controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tinkoff.fintechlab.configuration.LanguageCode;

@Controller
public class TextController {
    private final TranslateController translateController;

    public TextController(TranslateController translateController) {
        this.translateController = translateController;
    }

    @RequestMapping("/")
    public String home(Model m) {
        m.addAttribute("languages", LanguageCode.values());
        return "index";
    }

    @GetMapping("/translated")
    public String add(@RequestParam("sourceLanguage") String sourceLanguage, @RequestParam("targetLanguage") String targetLanguage,@RequestParam("textForTranslation") String text, Model model) throws JSONException {

        String translatedText = translateController.translate("ru", "en", text); //TODO добавить преобразование языков

        model.addAttribute("sourceText", text);
        model.addAttribute("translatedText", translatedText); // Добавляем значение в модель под ключом "message"
        return "index"; // Возвращаем имя шаблона без расширения (.html)
    }
}