package org.tinkoff.fintechlab.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tinkoff.fintechlab.dto.Request;

/**
 * RequestRepository is a Spring repository responsible for performing database operations related to translation requests.
 * It uses a JdbcClient to interact with the database and log relevant information.
 */
@Repository
@AllArgsConstructor
public class RequestRepository {

    /** JdbcClient instance for executing SQL queries */
    private final JdbcClient jdbcClient;

    /** Logger instance for logging information */
    private static final Logger logger = LoggerFactory.getLogger(RequestRepository.class.getName());

    /**
     * Adds a new translation request to the database.
     *
     * @param request the translation request to add
     */
    @Transactional
    public void add(Request request) {
        String sql =
                "INSERT INTO requests (ip_address, input_text, translated_text) VALUES (:ip_address, :input_text, :translated_text)";

        var inetAddress = request.getIpAddress().toString();
        logger.info("This is IP: " + inetAddress);

        jdbcClient.sql(sql)
                .param("ip_address", inetAddress)
                .param("input_text", request.getInputText())
                .param("translated_text", request.getTranslatedText())
                .update();
    }
}
