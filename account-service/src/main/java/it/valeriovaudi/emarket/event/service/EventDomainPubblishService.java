package it.valeriovaudi.emarket.event.service;

import it.valeriovaudi.emarket.event.factory.DomainEventFactory;
import it.valeriovaudi.emarket.event.model.*;
import it.valeriovaudi.emarket.event.repository.*;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by mrflick72 on 04/05/17.
 */

@Service
public class EventDomainPubblishService {

    private final DomainEventFactory domainEventFactory;
    private final EventAuditDataRepository eventAuditDataRepository;
    private final MessageChannel accountEventOutboundChannel;

    public EventDomainPubblishService(DomainEventFactory domainEventFactory,
                                      MessageChannel accountEventOutboundChannel,
                                      EventAuditDataRepository eventAuditDataRepository) {
        this.domainEventFactory = domainEventFactory;
        this.eventAuditDataRepository = eventAuditDataRepository;
        this.accountEventOutboundChannel = accountEventOutboundChannel;
    }

    public EventAuditData publishEventAuditData(EventType eventType, Map<String, String> messages) {
        EventAuditData event = domainEventFactory.newEventAuditData(eventType, messages);
        eventAuditDataRepository.save(event);
        accountEventOutboundChannel.send(MessageBuilder.withPayload(event).build());

        return event;
    }
}