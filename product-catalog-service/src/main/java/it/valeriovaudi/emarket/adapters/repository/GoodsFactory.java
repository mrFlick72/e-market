package it.valeriovaudi.emarket.adapters.repository;

import it.valeriovaudi.emarket.domain.model.*;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class GoodsFactory {

    public Goods newGoodsFor(Document document) {
        return new Goods(barCodeFor(document),
                goodsNameFor(document),
                goodsDescriptionFor(document),
                priceFor(document));
    }

    public Document newMongoDocumentFor(Goods goods) {
        Document updatedDocument = new Document();
        updatedDocument.append("barCode", goods.getBarCode().getContent());
        updatedDocument.append("name", goods.getName().getContent());
        updatedDocument.append("price", goods.getPrice());
        updatedDocument.append("descriptions", goods.getDescriptions());
        System.out.println("updatedDocument");
        System.out.println(updatedDocument);
        return updatedDocument;
    }

    private Price priceFor(Document document) {
        Document price = document.get("price", Document.class);
        return new Price(
                new BigDecimal(price.get("value").toString()),
                price.get("currency").toString()
        );
    }

    private List<GoodsDescription> goodsDescriptionFor(Document document) {
        return document.getList("descriptions", Document.class)
                .stream()
                .map(hashMap -> new GoodsDescription(
                        hashMap.get("name").toString(),
                        hashMap.get("content").toString()
                ))
                .collect(Collectors.toList());
    }

    private GoodsName goodsNameFor(Document document) {
        return new GoodsName(document.getString("name"));
    }

    private BarCode barCodeFor(Document document) {
        return new BarCode(document.getString("barCode"));
    }
}
