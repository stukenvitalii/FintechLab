package org.tinkoff.fintechlab.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.tinkoff.fintechlab.exception.ClientException;

@Controller
public class TranslateController {
    @Value("${app.yandex-api-url}")
    private String API_URL;

    @Value("${app.api-token}")
    private String API_KEY;

    @Value("${app.folder-id}")
    private String FOLDER_ID;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(TranslateController.class.getName());

    public String translate(String sourceLanguage, String targetLanguage, String text) throws JSONException, ClientException, RuntimeException {
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
                logger.error(e.getMessage());
                throw new RuntimeException("Failed to parse translation response", e);
            }
        } else {
            logger.error("Translation request failed: " + response.getStatusCode());
            throw new ClientException("Translation request failed", response.getStatusCode().value());
        }
    }


}
