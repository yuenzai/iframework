package cn.ecosync.iframework.outbox.repository.jpa;

import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.event.repository.EventRepository;
import cn.ecosync.iframework.outbox.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yuenzai
 * @since 2024
 */
public interface OutboxEventJpaRepository extends JpaRepository<OutboxEvent, Integer>, EventRepository {
    @Override
    default void add(Event event) {
        OutboxEvent outboxEvent;
        if (event instanceof OutboxEvent) {
            outboxEvent = (OutboxEvent) event;
        } else {
            outboxEvent = new OutboxEvent(event);
        }
        save(outboxEvent);
    }
}
