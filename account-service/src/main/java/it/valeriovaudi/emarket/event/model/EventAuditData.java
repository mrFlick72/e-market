package it.valeriovaudi.emarket.event.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by mrflick72 on 03/05/17.
 */

@Data
@Table
@ToString
public class EventAuditData implements Serializable {

    @PrimaryKey
    protected UUID id;

    private String traceId;
    private String userName;
    private Date timeStamp;

    private EventType eventType;
    private Map<String, String> messages;
}
