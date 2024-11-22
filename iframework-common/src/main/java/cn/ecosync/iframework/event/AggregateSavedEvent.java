package cn.ecosync.iframework.event;

import cn.ecosync.iframework.domain.AggregateRoot;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.util.Assert;

/**
 * @author yuenzai
 * @since 2024
 */
public class AggregateSavedEvent extends AbstractEvent {
    @JsonUnwrapped
    private final AggregateRoot aggregateRoot;

    public AggregateSavedEvent(AggregateRoot aggregateRoot) {
        Assert.notNull(aggregateRoot, "aggregateRoot can not be null");
        this.aggregateRoot = aggregateRoot;
    }

    @Override
    public String aggregateType() {
        return aggregateRoot.aggregateType();
    }

    @Override
    public String aggregateId() {
        return aggregateRoot.aggregateId();
    }

    @Override
    public String eventType() {
        return "";
    }

    public AggregateRoot aggregateRoot() {
        return aggregateRoot;
    }

    @Override
    public String toString() {
        return "AggregateSavedEvent{" +
                "aggregateRoot=" + aggregateRoot +
                '}';
    }
}
