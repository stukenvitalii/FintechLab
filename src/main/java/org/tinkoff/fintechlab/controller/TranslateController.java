package org.tinkoff.fintechlab.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.tinkoff.fintechlab.dto.TranslateResponse;
import org.tinkoff.fintechlab.dto.TranslatedWord;

import java.util.stream.Collectors;

@Controller
public class TranslateController {
    @Value("${app.yandex-api-url}")
    private String API_URL;

    @Value("${app.api-token}")
    private String API_KEY;

    @Value("${app.folder-id}")
    private String FOLDER_ID;

    private final RestTemplate restTemplate = new RestTemplate();

    public String translate(String sourceLanguage, String targetLanguage, String text) throws JSONException, RuntimeException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("folderId", FOLDER_ID);
        requestParams.put("sourceLanguageCode", sourceLanguage);
        requestParams.put("targetLanguageCode", targetLanguage);
        requestParams.put("texts", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(requestParams.toString(), headers);

        TranslateResponse translatedText =
                restTemplate.postForObject(API_URL, entity, TranslateResponse.class);

        return translatedText.getTranslations().stream()
                .map(TranslatedWord::getText)
                .collect(Collectors.joining(" "));
    }


}
