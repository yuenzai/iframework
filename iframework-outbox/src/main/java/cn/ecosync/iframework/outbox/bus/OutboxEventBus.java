package cn.ecosync.iframework.outbox.bus;

import cn.ecosync.iframework.event.AbstractEventBus;
import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.outbox.domain.Outbox;
import cn.ecosync.iframework.outbox.repository.OutboxJpaRepository;
import cn.ecosync.iframework.serde.JsonSerde;
import org.springframework.util.Assert;

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
        String payload = jsonSerde.serialize(event);
        return new Outbox(
                event.eventId(),
                event.eventDestination(),
                event.eventKey(),
                event.eventTime(),
                payload
        );
    }
}
