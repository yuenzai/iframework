package cn.ecosync.iframework.event;

import java.time.Instant;

/**
 * @author yuenzai
 * @since 2024
 */
public interface Event {
    String eventId();

    String eventDestination();

    String eventKey();

    Instant eventTime();
}
