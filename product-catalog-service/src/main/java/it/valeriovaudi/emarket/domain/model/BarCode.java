package it.valeriovaudi.emarket.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class BarCode {

    private final String content;

    public BarCode(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
