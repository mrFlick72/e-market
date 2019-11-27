package it.valeriovaudi.emarket.adapters.repository;

import it.valeriovaudi.emarket.domain.model.*;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

    @BeforeEach
    void setUp() {
        goodsRepository = new MongoGoodsRepository(new GoodsFactory(), reactiveMongoOperations);
    }

    @Test
    void saveANewGoods() {
        Goods savedGoods = Flux.from(goodsRepository.save(goods)).blockLast(Duration.ofMinutes(1));

        assertEquals(savedGoods, goods);
    }

    @Test
    void findANewGoods() {
        Goods savedGoods = Flux.from(goodsRepository.save(goods)).blockLast(Duration.ofMinutes(1));

        Goods foundGoods = Mono.from(goodsRepository.findBy(goods.getBarCode())).block(Duration.ofMinutes(1));

        assertEquals(savedGoods, foundGoods);
    }
}