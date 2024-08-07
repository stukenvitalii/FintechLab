package org.tinkoff.fintechlab.dto;

import lombok.Data;

import java.util.List;

/**
 * TranslateResponse represents the response from the translation service.
 * It contains a list of translated words.
 */
@Data
public class TranslateResponse {

    /** A list of translated words */
    private List<TranslatedWord> translations;
}
