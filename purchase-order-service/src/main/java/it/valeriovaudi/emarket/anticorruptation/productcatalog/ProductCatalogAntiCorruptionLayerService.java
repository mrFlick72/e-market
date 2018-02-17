package it.valeriovaudi.emarket.anticorruptation.productcatalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.valeriovaudi.emarket.anticorruptation.AbstractAntiCorruptionLayerService;
import it.valeriovaudi.emarket.anticorruptation.AntiCorruptionLayerStrategy;
import it.valeriovaudi.emarket.model.Goods;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mrflick72 on 30/05/17.
 */

@Service
public class ProductCatalogAntiCorruptionLayerService extends AbstractAntiCorruptionLayerService {

    private Map<String, AntiCorruptionLayerStrategy> productCatalogAntiCorruptationRegistry;
    private final ProductCatalogAntiCorruptionLayerServiceHalJsonStrategy productCatalogAntiCorruptionLayerServiceHalJsonStrategy;


    public ProductCatalogAntiCorruptionLayerService(ObjectMapper objectMapper,
                                                    ProductCatalogAntiCorruptionLayerServiceHalJsonStrategy productCatalogAntiCorruptionLayerServiceHalJsonStrategy1) {
        super(objectMapper);
        this.productCatalogAntiCorruptionLayerServiceHalJsonStrategy = productCatalogAntiCorruptionLayerServiceHalJsonStrategy1;
    }

    @PostConstruct
    public void init(){
        productCatalogAntiCorruptationRegistry = new ConcurrentHashMap<>();
        productCatalogAntiCorruptationRegistry.put(MediaType.APPLICATION_JSON_VALUE,productCatalogAntiCorruptionLayerServiceHalJsonStrategy);
        productCatalogAntiCorruptationRegistry.put(MediaType.APPLICATION_JSON_UTF8_VALUE,productCatalogAntiCorruptionLayerServiceHalJsonStrategy);
    }

    public Goods newGoods(String goods, String mediaType){
        return (Goods) Optional.ofNullable(productCatalogAntiCorruptationRegistry.get(mediaType))
                .map(anticCorruptationLayerStrategy -> anticCorruptationLayerStrategy.traslate(goods))
                .orElse(null);
    }
}
