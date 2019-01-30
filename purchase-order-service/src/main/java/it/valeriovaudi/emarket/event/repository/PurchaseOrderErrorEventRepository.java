package it.valeriovaudi.emarket.event.repository;

import it.valeriovaudi.emarket.event.model.PurchaseOrderErrorEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

/**
 * Created by mrflick72 on 30/05/17.
 */
public interface PurchaseOrderErrorEventRepository extends CassandraRepository<PurchaseOrderErrorEvent, UUID> {
}
