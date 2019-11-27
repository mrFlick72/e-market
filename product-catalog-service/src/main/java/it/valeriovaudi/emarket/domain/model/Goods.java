package it.valeriovaudi.emarket.domain.model;


import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
public class Goods {

    private final BarCode barCode;
    private final GoodsName name;
    private final List<GoodsDescription> descriptions;
    private final Price price;

    public Goods(BarCode barCode, GoodsName name, List<GoodsDescription> descriptions, Price price) {
        this.barCode = barCode;
        this.name = name;
        this.descriptions = descriptions;
        this.price = price;
    }

    public GoodsName getName() {
        return name;
    }

    public BarCode getBarCode() {
        return barCode;
    }

    public List<GoodsDescription> getDescriptions() {
        return descriptions;
    }

    public Price getPrice() {
        return price;
    }
}
