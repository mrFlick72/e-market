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
import reactor.core.publisher.Mono;

public class MongoGoodsRepository implements GoodsRepository {

    private static final String GOODS_COLLECTION_NAME = "goods";

    private final GoodsFactory goodsFactory;
    private final ReactiveMongoOperations mongoTemplate;

    public MongoGoodsRepository(GoodsFactory goodsFactory,
                                ReactiveMongoOperations mongoTemplate) {
        this.goodsFactory = goodsFactory;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Publisher<Goods> findBy(BarCode barCode) {
        return mongoTemplate.findOne(findOneFor(barCode), Document.class, GOODS_COLLECTION_NAME)
                .map(goodsFactory::newGoodsFor);
    }


    @Override
    public Publisher<Goods> findAll() {
        return mongoTemplate.findAll(Document.class, GOODS_COLLECTION_NAME)
                .map(goodsFactory::newGoodsFor);
    }

    @Override
    public Publisher<Goods> save(Goods goods) {
        return mongoTemplate.upsert(findOneFor(goods.getBarCode()),
                Update.fromDocument(goodsFactory.newMongoDocumentFor(goods)),
                GOODS_COLLECTION_NAME)
                .map(updateResult -> goods);
    }

    private Query findOneFor(BarCode barCode) {
        return Query.query(Criteria.where("barCode").in(barCode.getContent()));
    }


    @Override
    public Publisher<Void> delete(BarCode barCode) {
        return mongoTemplate.remove(findOneFor(barCode), GOODS_COLLECTION_NAME)
                .flatMap(deleteResult -> Mono.empty());
    }
}
