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

/**
 * TranslateController is a Spring MVC controller responsible for handling text translation requests.
 * It interacts with an external translation API to translate text from one language to another.
 */
@Controller
public class TranslateController {

    /** The URL of the Yandex translation API */
    @Value("${app.yandex-api-url}")
    private String API_URL;

    /** The API key for authenticating requests to the Yandex translation API */
    @Value("${app.api-token}")
    private String API_KEY;

    /** The folder ID used in the Yandex translation API request */
    @Value("${app.folder-id}")
    private String FOLDER_ID;

    /** RestTemplate instance for making HTTP requests */
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Translates text from a source language to a target language using the Yandex translation API.
     *
     * @param sourceLanguage the source language code
     * @param targetLanguage the target language code
     * @param text           the text to be translated
     * @return the translated text
     * @throws JSONException     if there is an error constructing the JSON request
     * @throws RuntimeException  if there is an error with the translation API request
     */
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

        TranslateResponse translatedText = restTemplate.postForObject(API_URL, entity, TranslateResponse.class);

        return translatedText.getTranslations().stream()
                .map(TranslatedWord::getText)
                .collect(Collectors.joining(" "));
    }

}
