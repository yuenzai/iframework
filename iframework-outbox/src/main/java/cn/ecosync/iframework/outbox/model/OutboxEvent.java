package cn.ecosync.iframework.outbox.model;

import cn.ecosync.iframework.domain.IdentifiedValueObject;
import cn.ecosync.iframework.event.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.util.Assert;

import java.time.Instant;

/**
 * @author yuenzai
 * @since 2024
 */
@Entity
@Table(name = "OUTBOX_EVENT")
public class OutboxEvent extends IdentifiedValueObject implements Event {
    @Column(name = "event_id", nullable = false, updatable = false)
    private String eventId;
    @Column(name = "event_destination", nullable = false, updatable = false)
    private String eventDestination;
    @Column(name = "event_key", nullable = false, updatable = false)
    private String eventKey;
    @Column(name = "event_time", nullable = false, updatable = false)
    private Long eventTime;
    @Column(name = "event_payload", nullable = false, updatable = false)
    private Event eventPayload;

    protected OutboxEvent() {
    }

    public OutboxEvent(Event event) {
        this(event.eventId(), event.eventDestination(), event.eventKey(), event.eventTime(), event);
    }

    private OutboxEvent(String eventId, String eventDestination, String eventKey, Instant eventTime, Event eventPayload) {
        Assert.notNull(eventId, "eventId must not be null");
        Assert.notNull(eventDestination, "eventDestination must not be null");
        Assert.notNull(eventKey, "eventKey must not be null");
        Assert.notNull(eventTime, "eventTime must not be null");
        Assert.notNull(eventPayload, "eventPayload must not be null");
        this.eventId = eventId;
        this.eventDestination = eventDestination;
        this.eventKey = eventKey;
        this.eventTime = eventTime.toEpochMilli();
        this.eventPayload = eventPayload;
    }

    @Override
    public String eventId() {
        return eventId;
    }

    @Override
    public String eventDestination() {
        return eventDestination;
    }

    @Override
    public String eventKey() {
        return eventKey;
    }

    @Override
    public Instant eventTime() {
        return Instant.ofEpochMilli(eventTime);
    }
}
