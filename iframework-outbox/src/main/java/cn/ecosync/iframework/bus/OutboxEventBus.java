package cn.ecosync.iframework.bus;

import cn.ecosync.iframework.event.AbstractEventBus;
import cn.ecosync.iframework.event.AggregateRemovedEvent;
import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.outbox.Outbox;
import cn.ecosync.iframework.jpa.repository.OutboxJpaRepository;
import cn.ecosync.iframework.serde.JsonSerde;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author yuenzai
 * @since 2024
 */
public class OutboxEventBus extends AbstractEventBus {
    private final OutboxJpaRepository outboxJpaRepository;
    private final JsonSerde jsonSerde;

    public OutboxEventBus(OutboxJpaRepository outboxJpaRepository, JsonSerde jsonSerde) {
        this.outboxJpaRepository = outboxJpaRepository;
        this.jsonSerde = jsonSerde;
    }

    @Override
    public void publish(Event event) {
        Assert.notNull(event, "event must not be null");
        Outbox outbox = toOutbox(event);
        outboxJpaRepository.save(outbox);
    }

    private Outbox toOutbox(Event event) {
        String payload = Optional.of(event)
                .filter(in -> !(in instanceof AggregateRemovedEvent))
                .map(jsonSerde::serialize)
                .orElse(null);
        return new Outbox(
                event.aggregateType(),
                event.aggregateId(),
                event.eventId(),
                event.eventTime(),
                event.eventType(),
                payload
        );
    }
}
