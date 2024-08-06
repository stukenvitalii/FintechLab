package org.tinkoff.fintechlab.dto;

import lombok.Data;
import java.util.List;

@Data
public class TranslateResponse {

    private List<TranslatedWord> translations;
}
