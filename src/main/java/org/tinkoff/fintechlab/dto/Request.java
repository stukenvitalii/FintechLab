package org.tinkoff.fintechlab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.net.InetAddress;

@Table("requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    @Column("ip_address")
    private InetAddress ipAddress;

    @Column("input_text")
    private String inputText;

    @Column("translated_text")
    private String translatedText;
}
