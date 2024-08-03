package org.tinkoff.fintechlab.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        String yandexApiUrl,
        String apiToken,
        String folderId
) {}

