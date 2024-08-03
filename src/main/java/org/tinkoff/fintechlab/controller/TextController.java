package org.tinkoff.fintechlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TextController {
    @Autowired
    private TranslateController translateController;

    @RequestMapping("/")
    public String home(Model m) {
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