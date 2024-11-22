package cn.ecosync.iframework.event;

import java.time.Instant;

/**
 * @author yuenzai
 * @since 2024
 */
public interface Event {
    String aggregateType();

    String aggregateId();

    String eventId();

    Instant eventTime();

    String eventType();
}
