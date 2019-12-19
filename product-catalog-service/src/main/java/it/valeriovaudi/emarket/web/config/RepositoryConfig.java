package it.valeriovaudi.emarket.web.config;


import it.valeriovaudi.emarket.adapters.repository.GoodsFactory;
import it.valeriovaudi.emarket.adapters.repository.MongoGoodsRepository;
import it.valeriovaudi.emarket.domain.repository.GoodsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class RepositoryConfig {

    @Bean
    public GoodsRepository goodsRepository(ReactiveMongoTemplate reactiveMongoTemplate) {
        return new MongoGoodsRepository(new GoodsFactory(), reactiveMongoTemplate);
    }
}
