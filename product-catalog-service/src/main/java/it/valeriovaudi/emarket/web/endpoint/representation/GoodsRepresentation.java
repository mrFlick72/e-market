package it.valeriovaudi.emarket.web.endpoint.representation;


import it.valeriovaudi.emarket.domain.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRepresentation {

    private String barCode;
    private String name;
    private List<GoodsDescriptionRepresentation> descriptions;
    private PriceRepresentation price;

    public static GoodsRepresentation fromDomainToRepresentationFor(Goods goods) {
        return new GoodsRepresentation(
                goods.getBarCode().getContent(),
                goods.getName().getContent(),
                descriptionRepresentationFor(goods.getDescriptions()),
                new PriceRepresentation(goods.getPrice().getValue(), goods.getPrice().getCurrency()));
    }

    public Goods fromRepresentationToDomainWith(String barcode) {
        return new Goods(
                new BarCode(barcode),
                new GoodsName(this.name),
                descriptionFor(),
                new Price(price.getValue(), price.getCurrency()));
    }

    private List<GoodsDescription> descriptionFor() {
        return descriptions.stream().map(desc -> new GoodsDescription(desc.getName(), desc.getContent())).collect(Collectors.toList());
    }

    private static List<GoodsDescriptionRepresentation> descriptionRepresentationFor(List<GoodsDescription> descriptions) {
        return descriptions.stream().map(desc -> new GoodsDescriptionRepresentation(desc.getName(), desc.getContent())).collect(Collectors.toList());
    }
}
