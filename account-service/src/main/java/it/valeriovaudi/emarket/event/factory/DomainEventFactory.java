package it.valeriovaudi.emarket.event.factory;

import com.datastax.driver.core.utils.UUIDs;
import it.valeriovaudi.emarket.event.model.*;
import it.valeriovaudi.emarket.security.SecurityUtils;
import org.springframework.stereotype.Component;
import brave.Tracer;

import java.util.Date;
import java.util.Map;

/**
 * Created by mrflick72 on 03/05/17.
 */

@Component
public class DomainEventFactory {

    private final SecurityUtils securityUtils;
    private final Tracer tracer;

    public DomainEventFactory(SecurityUtils securityUtils, Tracer tracer) {
        this.securityUtils = securityUtils;
        this.tracer = tracer;
    }

    public EventAuditData newEventAuditData(EventType eventType, Map<String, String> messages) {
        EventAuditData event = new EventAuditData();
        event.setId(UUIDs.timeBased());
        event.setUserName(securityUtils.getPrincipalUserName());

        event.setTraceId(tracer.currentSpan().context().traceIdString());
        event.setTimeStamp(new Date());

        event.setEventType(eventType);
        event.setMessages(messages);
        return event;
    }

}
