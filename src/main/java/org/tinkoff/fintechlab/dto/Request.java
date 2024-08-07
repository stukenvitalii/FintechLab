package org.tinkoff.fintechlab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.net.InetAddress;

/**
 * Request is a data transfer object representing a translation request.
 * It is mapped to the "requests" table in the database.
 */
@Table("requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    /**
     * The IP address of the client making the request
     */
    @Column("ip_address")
    private InetAddress ipAddress;

    /**
     * The original text to be translated
     */
    @Column("input_text")
    private String inputText;

    /**
     * The translated text
     */
    @Column("translated_text")
    private String translatedText;
}