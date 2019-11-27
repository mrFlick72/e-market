package it.valeriovaudi.emarket.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class GoodsDescription {

    private final String name;
    private final String content;

    public GoodsDescription(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
