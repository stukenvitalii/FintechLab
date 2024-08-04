package org.tinkoff.fintechlab.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tinkoff.fintechlab.dto.Request;
import org.tinkoff.fintechlab.repository.RequestRepository;
import org.tinkoff.fintechlab.service.RequestService;

import java.net.UnknownHostException;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    @Override
    public void add(Request request) throws UnknownHostException {
        requestRepository.add(request);
    }
}
