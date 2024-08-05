package org.tinkoff.fintechlab.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientException extends Exception{
    private String message;
    private int statusCode;
}
