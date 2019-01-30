package it.valeriovaudi.emarket.event.repository;

import it.valeriovaudi.emarket.event.model.PurchaseOrderEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

/**
 * Created by mrflick72 on 07/06/17.
 */
public interface PurchaseOrderEventRepository extends CassandraRepository<PurchaseOrderEvent, UUID> {
}
