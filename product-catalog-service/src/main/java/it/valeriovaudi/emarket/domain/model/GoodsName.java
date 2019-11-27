package it.valeriovaudi.emarket.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class GoodsName {
    private final String content;

    public GoodsName(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
