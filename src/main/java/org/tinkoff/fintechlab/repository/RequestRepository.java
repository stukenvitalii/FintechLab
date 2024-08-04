package org.tinkoff.fintechlab.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tinkoff.fintechlab.dto.Request;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Repository
@AllArgsConstructor
public class RequestRepository {
    private final JdbcClient jdbcClient;
    private static final Logger logger = LoggerFactory.getLogger(RequestRepository.class.getName());


    @Transactional
    public void add(Request request) throws UnknownHostException {
        String sql =
                "INSERT INTO requests (ip_address, input_text, translated_text) VALUES (:ip_address,:input_text,:translated_text)";

//        var inetAddress = InetAddress.getByName("192.168.1.1");
        var inetAddress = InetAddress.getLocalHost().toString();
        logger.info("This is IP: " + inetAddress);
        jdbcClient.sql(sql)
                .param("ip_address", inetAddress)
                .param("input_text", request.getInputText())
                .param("translated_text", request.getTranslatedText())
                .update();
    }
}
