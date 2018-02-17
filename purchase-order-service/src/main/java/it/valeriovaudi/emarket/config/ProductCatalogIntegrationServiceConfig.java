package it.valeriovaudi.emarket.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by mrflick72 on 31/05/17.
 */
@Configuration
@RibbonClient("productCatalogIntegrationServiceConfig")
public class ProductCatalogIntegrationServiceConfig {

    @Bean
    @LoadBalanced
    public RestTemplate productCatalogIntegrationServiceRestTemplate(SpringClientFactory springClientFactory){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new RibbonClientHttpRequestFactory(springClientFactory));
        return restTemplate;
    }
}
