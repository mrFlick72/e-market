package it.valeriovaudi.emarket;

import it.valeriovaudi.emarket.domain.model.Goods;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TestUtils {

    public static Predicate<Goods> findAllPredicateMatcher(Goods... goods) {
        return Stream.of(goods)
                .<Predicate<Goods>>map((nextGoods) -> (goodsToPredicate) -> Objects.deepEquals(nextGoods, goodsToPredicate))
                .reduce((identity) -> false, Predicate::or);

    }

    public static void preparementTestScenarioFor(GoodsRepository goodsRepository, Goods... goods) {
        Stream.of(goods).forEachOrdered(item -> {
            StepVerifier.create(goodsRepository.save(item))
                    .expectNext(item)
                    .expectComplete()
                    .verify();
        });
    }
}
