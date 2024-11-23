package cn.ecosync.iframework.outbox.repository;

import cn.ecosync.iframework.event.EventAcknowledgment;
import cn.ecosync.iframework.outbox.domain.OutboxProcessed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

/**
 * @author yuenzai
 * @since 2024
 */
public interface OutboxProcessedJpaRepository extends JpaRepository<OutboxProcessed, Integer>, EventAcknowledgment {
    @Override
    default void acknowledge(String eventId) {
        Assert.hasText(eventId, "eventId can not be empty");
        save(new OutboxProcessed(eventId));
    }
}
