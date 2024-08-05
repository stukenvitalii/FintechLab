package org.tinkoff.fintechlab.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tinkoff.fintechlab.dto.Request;
import org.tinkoff.fintechlab.service.RequestService;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestService requestService;

    @Test
    public void testAddRequestRepositoryOnSuccess() throws UnknownHostException {
        Request request = new Request(InetAddress.getByName("0:0:0:0:0:0:0:1"), "привет", "hi");
        Mockito.doNothing().when(requestService).add(request);
        requestService.add(request);
        Mockito.verify(requestService).add(request);
    }

}