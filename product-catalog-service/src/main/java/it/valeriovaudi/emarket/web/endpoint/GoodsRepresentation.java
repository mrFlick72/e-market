package it.valeriovaudi.emarket.web.endpoint;


import it.valeriovaudi.emarket.domain.model.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoodsRepresentation {

    private String barCode;
    private String name;
    private List<GoodsDescriptionRepresentation> descriptions;
    private PriceRepresentation price;

    public Goods formRepresentationToDomainWith(String barcode){
        return new Goods(
                new BarCode(barcode),
                new GoodsName(this.name),
                descriptionFor(),
                new Price(price.getValue(), price.getCurrency()));
    }

    private List<GoodsDescription> descriptionFor() {
        return descriptions.stream().map(desc -> new GoodsDescription(desc.getName(),desc.getContent())).collect(Collectors.toList());
    }
}
