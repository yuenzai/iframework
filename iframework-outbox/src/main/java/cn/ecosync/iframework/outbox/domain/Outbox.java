package cn.ecosync.iframework.outbox.domain;

import cn.ecosync.iframework.domain.IdentifiedValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * @author yuenzai
 * @since 2024
 */
@Entity
@Table(name = "outbox")
public class Outbox extends IdentifiedValueObject {
    @Column(name = "event_id", nullable = false, updatable = false)
    private String eventId;
    @Column(name = "event_destination", nullable = false, updatable = false)
    private String eventDestination;
    @Column(name = "event_key", nullable = false, updatable = false)
    private String eventKey;
    @Column(name = "event_time", nullable = false, updatable = false)
    private Long eventTime;
    @Column(name = "event_payload", nullable = false, updatable = false)
    private String eventPayload;

    protected Outbox() {
    }

    public Outbox(String eventId, String eventDestination, String eventKey, Instant eventTime, String eventPayload) {
        this.eventId = eventId;
        this.eventDestination = eventDestination;
        this.eventKey = eventKey;
        this.eventTime = eventTime.toEpochMilli();
        this.eventPayload = eventPayload;
    }
}
