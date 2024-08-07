package org.tinkoff.fintechlab.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * LanguageCode is an enumeration representing various language codes along with their respective names in Russian.
 * It uses Lombok annotations for generating boilerplate code.
 */
@Getter
@AllArgsConstructor
public enum LanguageCode {
    RUSSIAN("Русский", "ru"),
    ENGLISH("Английский", "en"),
    SPANISH("Испанский", "es"),
    FRENCH("Французский", "fr"),
    GERMAN("Немецкий", "de"),
    CHINESE("Китайский", "zh"),
    JAPANESE("Японский", "ja"),
    ITALIAN("Итальянский", "it"),
    PORTUGUESE("Португальский", "pt"),
    HINDI("Хинди", "hi"),
    ARABIC("Арабский", "ar"),
    KOREAN("Корейский", "ko"),
    DUTCH("Голландский", "nl"),
    GREEK("Греческий", "el"),
    TURKISH("Турецкий", "tr"),
    SWEDISH("Шведский", "sv");

    /** The name of the language in Russian */
    private final String language;

    /** The code of the language */
    private final String code;

    /**
     * Returns a string representation of the language code.
     *
     * @return a string combining the language name and its code
     */
    @Override
    public String toString() {
        return language + "-" + code;
    }
}
