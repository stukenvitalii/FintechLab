package org.tinkoff.fintechlab.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tinkoff.fintechlab.dto.Request;
import org.tinkoff.fintechlab.repository.RequestRepository;
import org.tinkoff.fintechlab.service.RequestService;

import java.net.UnknownHostException;

/**
 * RequestServiceImpl is a Spring service implementation that handles operations related to translation requests.
 * It uses RequestRepository to perform database operations.
 */
@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    /** Repository for performing database operations on requests */
    private final RequestRepository requestRepository;

    /**
     * Adds a new translation request by delegating to the repository.
     *
     * @param request the translation request to add
     * @throws UnknownHostException if the client's IP address cannot be determined
     */
    @Override
    public void add(Request request) throws UnknownHostException {
        requestRepository.add(request);
    }
}
