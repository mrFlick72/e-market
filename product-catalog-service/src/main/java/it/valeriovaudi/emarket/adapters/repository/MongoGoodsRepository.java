package it.valeriovaudi.emarket.adapters.repository;

import it.valeriovaudi.emarket.domain.model.BarCode;
import it.valeriovaudi.emarket.domain.model.Goods;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class MongoGoodsRepository implements GoodsRepository {

    private final GoodsFactory goodsFactory;
    private final ReactiveMongoOperations mongoTemplate;

    public MongoGoodsRepository(GoodsFactory goodsFactory,
                                ReactiveMongoOperations mongoTemplate) {
        this.goodsFactory = goodsFactory;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Publisher<Goods> findBy(BarCode barCode) {
        return mongoTemplate.findOne(findOneFor(barCode), Document.class, "goods")
                .map(goodsFactory::newGoodsFor);
    }


    @Override
    public Publisher<Goods> findAll() {
        return null;
    }

    @Override
    public Publisher<Goods> save(Goods goods) {
        return mongoTemplate.upsert(findOneFor(goods.getBarCode()),
                Update.fromDocument(goodsFactory.newMongoDocumentFor(goods)),
                "goods")
                .map(updateResult -> goods);
    }

    private Query findOneFor(BarCode barCode) {
        return Query.query(Criteria.where("barCode").in(barCode.getContent()));
    }


    @Override
    public Publisher<Goods> delete(BarCode barCode) {
        return null;
    }
}
