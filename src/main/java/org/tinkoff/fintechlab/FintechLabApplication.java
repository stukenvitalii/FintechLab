package org.tinkoff.fintechlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.tinkoff.fintechlab.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@EnableJdbcRepositories(basePackages = "org.tinkoff.fintechlab.repository")
public class FintechLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintechLabApplication.class, args);
    }

}
