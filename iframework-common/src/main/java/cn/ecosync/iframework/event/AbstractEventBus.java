package cn.ecosync.iframework.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;

/**
 * @author yuenzai
 * @since 2024
 */
public abstract class AbstractEventBus implements EventBus, ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void handle(Event event) {
        Assert.notNull(applicationEventPublisher, "applicationEventPublisher is null");
        Assert.notNull(event, "event must not be null");
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
