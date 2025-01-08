package cn.ecosync.iframework.bus;

import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.event.EventBus;
import cn.ecosync.iframework.event.repository.EventRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * @author yuenzai
 * @since 2024
 */
public class DefaultEventBus implements EventBus, ApplicationEventPublisherAware {
    private final EventRepository eventRepository;
    private ApplicationEventPublisher eventPublisher;

    public DefaultEventBus(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void publish(Event event) {
        Assert.notNull(event, "event must not be null");
        if (eventRepository != null && TransactionSynchronizationManager.isActualTransactionActive()) {
            // 可以直接访问数据库的环境使用，也就是客户端和服务端处于同一个局域网内（Eventual Consistency）
            eventRepository.add(event);
        } else {
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
