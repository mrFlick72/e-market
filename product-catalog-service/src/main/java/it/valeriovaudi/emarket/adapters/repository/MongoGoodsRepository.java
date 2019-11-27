package it.valeriovaudi.emarket.adapters.repository;

import it.valeriovaudi.emarket.domain.model.*;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MongoGoodsRepository implements GoodsRepository {

    private final ReactiveMongoOperations mongoTemplate;

    public MongoGoodsRepository(ReactiveMongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Publisher<Goods> findBy(BarCode barCode) {
        return mongoTemplate.findOne(findOneFor(barCode), Document.class, "goods")
                .map(document -> new Goods(getBarCodeFor(document),
                        getGoodsNameFor(document),
                        getGoodsDescriptionFor(document),
                        getPriceFor(document)));
    }

    private Price getPriceFor(Document document) {
        Document price = document.get("price", Document.class);
        return new Price(
                new BigDecimal(price.get("value").toString()),
                price.get("currency").toString()
        );
    }

    private List<GoodsDescription> getGoodsDescriptionFor(Document document) {
        return document.getList("descriptions", Document.class)
                .stream()
                .map(hashMap -> new GoodsDescription(
                        hashMap.get("name").toString(),
                        hashMap.get("content").toString()
                ))
                .collect(Collectors.toList());
    }

    private GoodsName getGoodsNameFor(Document document) {
        return new GoodsName(document.getString("name"));
    }

    private BarCode getBarCodeFor(Document document) {
        return new BarCode(document.getString("barCode"));
    }

    @Override
    public Publisher<Goods> findAll() {
        return null;
    }

    @Override
    public Publisher<Goods> save(Goods goods) {
        return mongoTemplate.upsert(findOneFor(goods.getBarCode()),
                Update.fromDocument(getDocumentFor(goods)),
                "goods")
                .map(updateResult -> goods);
    }

    private Query findOneFor(BarCode barCode) {
        return Query.query(Criteria.where("barCode").in(barCode.getContent()));
    }

    private Document getDocumentFor(Goods goods) {
        Document updatedDocument = new Document();
        updatedDocument.append("barCode", goods.getBarCode().getContent());
        updatedDocument.append("name", goods.getName().getContent());
        updatedDocument.append("price", goods.getPrice());
        updatedDocument.append("descriptions", goods.getDescriptions());
        System.out.println("updatedDocument");
        System.out.println(updatedDocument);
        return updatedDocument;
    }

    @Override
    public Publisher<Goods> delete(BarCode barCode) {
        return null;
    }
}
