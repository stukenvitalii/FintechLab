package org.tinkoff.fintechlab.service;

import org.springframework.stereotype.Service;
import org.tinkoff.fintechlab.dto.Request;

import java.net.UnknownHostException;

@Service
public interface RequestService {
    void add(Request request) throws UnknownHostException;
}
