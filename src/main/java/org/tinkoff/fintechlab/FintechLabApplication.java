package org.tinkoff.fintechlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.tinkoff.fintechlab.configuration.ApplicationConfig;

/**
 * FintechLabApplication is the entry point of the Spring Boot application.
 * It enables configuration properties and JDBC repositories for the application.
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
@EnableJdbcRepositories(basePackages = "org.tinkoff.fintechlab.repository")
public class FintechLabApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(FintechLabApplication.class, args);
    }
}
