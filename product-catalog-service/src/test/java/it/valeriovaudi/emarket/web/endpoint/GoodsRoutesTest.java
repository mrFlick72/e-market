package it.valeriovaudi.emarket.web.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.emarket.domain.model.*;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class GoodsRoutesTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private GoodsRepository goodsRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void findByBarCode() throws JsonProcessingException {

        Goods goods = new Goods(new BarCode("A_BARCODE"),
                new GoodsName(""),
                emptyList(),
                new Price(BigDecimal.ZERO, ""
                )
        );

        Publisher<Goods> save = goodsRepository.save(goods);

        StepVerifier.create(save)
                .expectNext(goods)
                .expectComplete()
                .verify();

        webClient.get().uri("/goods/A_BARCODE")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json(objectMapper.writeValueAsString(goods));
    }


}