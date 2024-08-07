package org.tinkoff.fintechlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * RestTemplateResponseErrorHandler is a Spring component that handles errors encountered during REST API calls using RestTemplate.
 * It throws appropriate exceptions for client and server errors.
 */
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    /**
     * Checks if the HTTP response has an error.
     *
     * @param httpResponse the HTTP response to check
     * @return true if the response has a client or server error, false otherwise
     * @throws IOException if an I/O error occurs
     */
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().is5xxServerError()
                || httpResponse.getStatusCode().is4xxClientError();
    }

    /**
     * Handles the error in the HTTP response by throwing appropriate exceptions.
     *
     * @param httpResponse the HTTP response containing the error
     * @throws IOException if an I/O error occurs
     * @throws HttpServerErrorException if a server error occurs
     * @throws HttpClientErrorException if a client error occurs
     */
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
