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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
        Publisher<Goods> savedGoods = goodsRepository.save(goods);
        StepVerifier.create(savedGoods)
                .expectNext(goods)
                .expectComplete()
                .verify();
    }

    @Test
    void findANewGoods() {
        Publisher<Goods> savedGoods = goodsRepository.save(goods);
        StepVerifier.create(savedGoods)
                .expectNext(goods)
                .expectComplete()
                .verify();

        Publisher<Goods> foundGoods = goodsRepository.findBy(goods.getBarCode());
        StepVerifier.create(foundGoods)
                .expectNext(goods)
                .expectComplete()
                .verify();
    }

    @Test
    void findAllGoods() {
        Publisher<Goods> goodsPublisher = goodsRepository.save(goods);
        StepVerifier.create(goodsPublisher)
                .expectNext(goods)
                .expectComplete()
                .verify();

        Publisher<Goods> anotherGoodsPublisher = goodsRepository.save(anotherGoods);
        StepVerifier.create(anotherGoodsPublisher)
                .expectNext(anotherGoods)
                .expectComplete()
                .verify();

        Publisher<Goods> findAllPublisher = goodsRepository.findAll();
        StepVerifier.create(findAllPublisher)
                .expectNext(goods, anotherGoods)
                .expectComplete()
                .verify();
    }

    @Test
    void deleteAGoods() {
        Publisher<Goods> goodsPublisher = goodsRepository.save(goods);
        StepVerifier.create(goodsPublisher)
                .expectNext(goods)
                .expectComplete()
                .verify();

        Publisher<Goods> anotherGoodsPublisher = goodsRepository.save(anotherGoods);
        StepVerifier.create(anotherGoodsPublisher)
                .expectNext(anotherGoods)
                .expectComplete()
                .verify();


        Publisher<Void> delteGoodsPublisher = goodsRepository.delete(new BarCode("A_BAR_CODE"));
        StepVerifier.create(delteGoodsPublisher)
                .expectComplete()
                .verify();


        Publisher<Goods> findAllPublisher = goodsRepository.findAll();
        StepVerifier.create(findAllPublisher)
                .expectNext(anotherGoods)
                .expectComplete()
                .verify();
    }
}