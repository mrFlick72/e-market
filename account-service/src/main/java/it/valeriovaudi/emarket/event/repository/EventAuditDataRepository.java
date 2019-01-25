package it.valeriovaudi.emarket.event.repository;

import it.valeriovaudi.emarket.event.model.EventAuditData;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface EventAuditDataRepository extends CassandraRepository<EventAuditData, UUID> {
}
