package it.valeriovaudi.emarket.web.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.emarket.TestUtils;
import it.valeriovaudi.emarket.domain.model.BarCode;
import it.valeriovaudi.emarket.domain.model.Goods;
import it.valeriovaudi.emarket.domain.model.GoodsName;
import it.valeriovaudi.emarket.domain.model.Price;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;

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
    public void findByBarCode() throws JsonProcessingException {

        Goods goods = new Goods(new BarCode("A_BARCODE"),
                new GoodsName(""),
                emptyList(),
                new Price(BigDecimal.ZERO, "")
        );

        TestUtils.preparementTestScenarioFor(goodsRepository, goods);

        webClient.get().uri("/goods/A_BARCODE")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().json(objectMapper.writeValueAsString(goods));
    }

    @Test
    public void saveANewGoods() throws JsonProcessingException {

        GoodsRepresentation goodsRepresentation = new GoodsRepresentation();
        goodsRepresentation.setBarCode("A_BARCODE");
        goodsRepresentation.setName("A_NAME");
        goodsRepresentation.setDescriptions(emptyList());
        PriceRepresentation price = new PriceRepresentation();
        price.setValue(BigDecimal.ZERO);
        price.setCurrency("A_CURRENCY");
        goodsRepresentation.setPrice(price);

        webClient.put().uri("/goods/A_BARCODE")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(goodsRepresentation)))
                .exchange()
                .expectStatus().isNoContent();
    }


}