package it.valeriovaudi.emarket.web.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.emarket.TestUtils;
import it.valeriovaudi.emarket.domain.model.BarCode;
import it.valeriovaudi.emarket.domain.model.Goods;
import it.valeriovaudi.emarket.domain.model.GoodsName;
import it.valeriovaudi.emarket.domain.model.Price;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import it.valeriovaudi.emarket.web.endpoint.representation.GoodsRepresentation;
import it.valeriovaudi.emarket.web.endpoint.representation.PriceRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class GoodsRoutesTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private GoodsRepository goodsRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void findAll() throws JsonProcessingException {
        TestUtils.preparementTestScenarioFor(goodsRepository,
                aGoods("A_BARCODE"),
                aGoods("ANOTHER_BARCODE")
        );

        String expectedJson = objectMapper.writeValueAsString(
                asList(
                        aGoodsRepresentation("A_BARCODE"),
                        aGoodsRepresentation("ANOTHER_BARCODE")
                )
        );

        webClient.get().uri("/goods")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json(expectedJson);
    }

    @Test
    public void findByBarCode() throws JsonProcessingException {
        TestUtils.preparementTestScenarioFor(goodsRepository, aGoods("A_BARCODE"));

        webClient.get().uri("/goods/A_BARCODE")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json(objectMapper.writeValueAsString(aGoodsRepresentation("A_BARCODE")));
    }

    @Test
    public void saveANewGoods() throws JsonProcessingException {

        GoodsRepresentation goodsRepresentation = aGoodsRepresentation("A_BARCODE");

        webClient.put().uri("/goods/A_BARCODE")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(goodsRepresentation)))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void deleteAGoods() {
        Goods goods = aGoods("A_BARCODE");
        TestUtils.preparementTestScenarioFor(goodsRepository, goods);

        webClient.delete().uri("/goods/A_BARCODE")
                .exchange()
                .expectStatus().isNoContent();

        Publisher<Goods> foundGoods = goodsRepository.findBy(goods.getBarCode());
        StepVerifier.create(foundGoods)
                .expectComplete()
                .verify();

    }

    private Goods aGoods(String barcode) {
        return new Goods(new BarCode(barcode),
                new GoodsName("A_NAME"),
                emptyList(),
                new Price(BigDecimal.ZERO, "A_CURRENCY")
        );
    }

    private GoodsRepresentation aGoodsRepresentation(String barcode) {
        GoodsRepresentation goodsRepresentation = new GoodsRepresentation();
        goodsRepresentation.setBarCode(barcode);
        goodsRepresentation.setName("A_NAME");
        goodsRepresentation.setDescriptions(emptyList());

        goodsRepresentation.setPrice(aPriceRepresentation());

        return goodsRepresentation;
    }

    private PriceRepresentation aPriceRepresentation() {
        PriceRepresentation price = new PriceRepresentation();
        price.setValue(BigDecimal.ZERO);
        price.setCurrency("A_CURRENCY");
        return price;
    }

}