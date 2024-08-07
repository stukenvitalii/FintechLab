package org.tinkoff.fintechlab.service;

import org.springframework.stereotype.Service;
import org.tinkoff.fintechlab.dto.Request;

import java.net.UnknownHostException;

/**
 * RequestService is a Spring service interface responsible for handling operations related to translation requests.
 */
@Service
public interface RequestService {

    /**
     * Adds a new translation request.
     *
     * @param request the translation request to add
     * @throws UnknownHostException if the client's IP address cannot be determined
     */
    void add(Request request) throws UnknownHostException;
}
