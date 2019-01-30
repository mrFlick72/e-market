package it.valeriovaudi.emarket.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestTemplate;

/**
 * Created by mrflick72 on 31/05/17.
 */
@Configuration
@RibbonClient("productCatalogIntegrationServiceConfig")
public class ProductCatalogIntegrationServiceConfig {

    @Bean
    @LoadBalanced
    public RestTemplate accountIntegrationServiceRestTemplate(SpringClientFactory springClientFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new RibbonClientHttpRequestFactory(springClientFactory));
        restTemplate.getInterceptors().add((request, bytes, execution) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            JwtAuthenticationToken oauthToken = (JwtAuthenticationToken) authentication;

            request.getHeaders().add("Authorization", "Bearer " + oauthToken.getToken().getTokenValue());
            return execution.execute(request, bytes);
        });

        return restTemplate;
    }
}
