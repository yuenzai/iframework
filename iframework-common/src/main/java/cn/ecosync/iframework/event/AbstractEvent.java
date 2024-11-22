package cn.ecosync.iframework.event;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * @author yuenzai
 * @since 2024
 */
public abstract class AbstractEvent implements Event {
    private final String eventId = UUID.randomUUID().toString().replace("-", "");

    private final Instant eventTime = Instant.now();

    @Override
    public String eventId() {
        return eventId;
    }

    @Override
    public Instant eventTime() {
        return eventTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEvent)) return false;
        AbstractEvent that = (AbstractEvent) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(eventId);
    }
}
