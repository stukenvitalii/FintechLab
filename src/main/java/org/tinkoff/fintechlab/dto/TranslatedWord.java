package org.tinkoff.fintechlab.dto;

import lombok.Data;

/**
 * TranslatedWord represents a single translated word returned by the translation service.
 * It is used to encapsulate the translated text.
 */
@Data
public class TranslatedWord {

    /** The translated text */
    private String text;
}
