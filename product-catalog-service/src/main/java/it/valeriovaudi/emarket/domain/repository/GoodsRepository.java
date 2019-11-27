package it.valeriovaudi.emarket.domain.repository;

import it.valeriovaudi.emarket.domain.model.BarCode;
import it.valeriovaudi.emarket.domain.model.Goods;
import org.reactivestreams.Publisher;


public interface GoodsRepository {

    Publisher<Goods> findBy(BarCode barCode);

    Publisher<Goods> findAll();

    Publisher<Goods> save(Goods goods);

    Publisher<Goods> delete(BarCode barCode);
}
