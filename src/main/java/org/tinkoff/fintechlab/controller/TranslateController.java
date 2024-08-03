package org.tinkoff.fintechlab.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class TranslateController {
    @Value("${app.yandex-api-url}")
    private String API_URL;

    @Value("${app.api-token}")
    private String API_KEY; // Укажите ваш API ключ

    @Value("${app.folder-id}")
    private String FOLDER_ID; // Укажите ваш folderId

    private final RestTemplate restTemplate = new RestTemplate();

    public String translate(String sourceLanguage, String targetLanguage, String text) throws JSONException {
        JSONObject requestParams = new JSONObject();
        requestParams.put("folderId", FOLDER_ID);
        requestParams.put("sourceLanguageCode", sourceLanguage);
        requestParams.put("targetLanguageCode", targetLanguage);
        requestParams.put("texts", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(requestParams.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("translations").get(0).path("text").asText();
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse translation response", e);
            }
        } else {
            throw new RuntimeException("Translation request failed: " + response.getStatusCode());
        }
    }


}
