package org.tinkoff.fintechlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is5xxServerError()
                || httpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().is5xxServerError()) {
            throw new HttpServerErrorException(httpResponse.getStatusCode());
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new HttpClientErrorException(httpResponse.getStatusCode());
            }
        }
    }
}
