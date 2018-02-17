package it.valeriovaudi.emarket.integration;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import it.valeriovaudi.emarket.anticorruptation.productcatalog.ProductCatalogAntiCorruptionLayerService;
import it.valeriovaudi.emarket.model.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by mrflick72 on 30/05/17.
 */

@Slf4j
@Service
public class ProductCatalogIntegrationService extends AbstractIntegrationService {

    private final ProductCatalogAntiCorruptionLayerService productCatalogAntiCorruptionLayerService;
    private final RestTemplate productCatalogIntegrationServiceRestTemplate;

    @Value("${external-service.base-uri-schema.goods-in-product-catalog}")
    private String goodsInProductCatalogServiceUriSchema;

    public ProductCatalogIntegrationService(ProductCatalogAntiCorruptionLayerService productCatalogAntiCorruptionLayerService,
                                            RestTemplate productCatalogIntegrationServiceRestTemplate) {
        this.productCatalogAntiCorruptionLayerService = productCatalogAntiCorruptionLayerService;
        this.productCatalogIntegrationServiceRestTemplate = productCatalogIntegrationServiceRestTemplate;
    }

    @HystrixCommand(fallbackMethod = "fallback", commandProperties = {@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE")})
    public Goods getGoodsInPriceListData(String priceListId, String goodsId){
        URI uri = UriComponentsBuilder.fromHttpUrl(goodsInProductCatalogServiceUriSchema)
                .buildAndExpand(priceListId, goodsId).toUri();

        ResponseEntity<String> serviceCall =
                productCatalogIntegrationServiceRestTemplate.exchange(newRequestEntity(uri), String.class);

        return productCatalogAntiCorruptionLayerService.newGoods(serviceCall.getBody(),
                serviceCall.getHeaders().getContentType().toString());
    }

    /**
     * fallback
     * */
    public Goods fallback(String priceListId, String goodsId) {
        return null;
    }
}
