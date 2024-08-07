package org.tinkoff.fintechlab.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * ApplicationConfig is a configuration class that binds the application properties
 * prefixed with "app" to the corresponding fields.
 * It uses Spring's @ConfigurationProperties and @Validated annotations for property binding and validation.
 */
@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        String yandexApiUrl,
        String apiToken,
        String folderId
) {}
