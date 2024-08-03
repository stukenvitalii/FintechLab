package org.tinkoff.fintechlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.tinkoff.fintechlab.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class FintechLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintechLabApplication.class, args);
    }

}
