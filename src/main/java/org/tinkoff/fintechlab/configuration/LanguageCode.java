package org.tinkoff.fintechlab.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    private final String language;
    private final String code;

    @Override
    public String toString() {
        return language + "-" + code;
    }
}
