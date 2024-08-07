package org.tinkoff.fintechlab.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.tinkoff.fintechlab.exception.RestTemplateResponseErrorHandler;

/**
 * RestTemplateConfig is a configuration class that sets up a {@link RestTemplate} bean with a custom error handler.
 * This configuration is used for making HTTP requests and handling errors during REST API calls.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a {@link RestTemplate} bean with a custom error handler.
     *
     * @return a configured instance of {@link RestTemplate}
     */
    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }
}
