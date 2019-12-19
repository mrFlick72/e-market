package it.valeriovaudi.emarket.adapters.repository;

import it.valeriovaudi.emarket.domain.model.*;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.function.Predicate;

import static it.valeriovaudi.emarket.TestUtils.findAllPredicateMatcher;
import static it.valeriovaudi.emarket.TestUtils.preparementTestScenarioFor;


@DataMongoTest
@ExtendWith(SpringExtension.class)
class MongoGoodsRepositoryTest {

    @Autowired
    private ReactiveMongoOperations reactiveMongoOperations;

    private GoodsRepository goodsRepository;

    private final Goods goods = new Goods(new BarCode("A_BAR_CODE"),
            new GoodsName("A_GOODS_NAME"),
            Collections.singletonList(new GoodsDescription("GOODS_DESCRIPTION", "CONTENT")),
            new Price(BigDecimal.ONE, "EUR"));

    private final Goods anotherGoods = new Goods(new BarCode("ANOTHER_BAR_CODE"),
            new GoodsName("A_GOODS_NAME"),
            Collections.singletonList(new GoodsDescription("GOODS_DESCRIPTION", "CONTENT")),
            new Price(BigDecimal.ONE, "EUR"));

    @BeforeEach
    void setUp() {
        goodsRepository = new MongoGoodsRepository(new GoodsFactory(), reactiveMongoOperations);
    }

    @Test
    void saveANewGoods() {
        preparementTestScenarioFor(goodsRepository, goods);
    }


    @Test
    void findANewGoods() {
        preparementTestScenarioFor(goodsRepository, goods);

        Publisher<Goods> foundGoods = goodsRepository.findBy(goods.getBarCode());
        StepVerifier.create(foundGoods)
                .expectNext(goods)
                .expectComplete()
                .verify();
    }

    @Test
    void findAllGoods() {
        preparementTestScenarioFor(goodsRepository, goods, anotherGoods);

        Publisher<Goods> findAllPublisher = goodsRepository.findAll();

        Predicate<Goods> findAllPredicateMatcher = findAllPredicateMatcher(goods, anotherGoods);
        StepVerifier.create(findAllPublisher)
                .expectNextMatches(findAllPredicateMatcher)
                .expectNextMatches(findAllPredicateMatcher)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteAGoods() {
        preparementTestScenarioFor(goodsRepository, goods, anotherGoods);

        Publisher<Void> deleteGoodsPublisher = goodsRepository.delete(new BarCode("A_BAR_CODE"));
        StepVerifier.create(deleteGoodsPublisher)
                .expectComplete()
                .verify();

        Publisher<Goods> findAllPublisher = goodsRepository.findAll();
        StepVerifier.create(findAllPublisher)
                .expectNext(anotherGoods)
                .expectComplete()
                .verify();
    }

}