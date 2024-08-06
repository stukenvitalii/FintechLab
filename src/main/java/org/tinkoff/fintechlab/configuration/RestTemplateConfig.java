package org.tinkoff.fintechlab.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.tinkoff.fintechlab.exception.RestTemplateResponseErrorHandler;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }
}
