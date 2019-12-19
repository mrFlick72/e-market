package it.valeriovaudi.emarket.web.endpoint;

import it.valeriovaudi.emarket.domain.model.BarCode;
import it.valeriovaudi.emarket.domain.model.Goods;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class GoodsRoutes {

    @Bean
    public RouterFunction<?> routes(GoodsRepository goodsRepository) {
        return RouterFunctions.route()
                .GET("/goods/{barcode}",
                        serverRequest ->
                                Mono.from(goodsRepository.findBy(new BarCode(serverRequest.pathVariable("barcode"))))
                                        .flatMap(goods -> ServerResponse.ok().body(BodyInserters.fromValue(goods)))
                )
                .build();
    }
}