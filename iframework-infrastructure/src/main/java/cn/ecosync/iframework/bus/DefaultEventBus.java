package cn.ecosync.iframework.bus;

import cn.ecosync.iframework.event.Event;
import cn.ecosync.iframework.event.EventBus;
import cn.ecosync.iframework.event.repository.EventRepository;
import cn.ecosync.iframework.serde.JsonSerde;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.util.concurrent.CompletableFuture;

/**
 * @author yuenzai
 * @since 2024
 */
public class DefaultEventBus extends StompSessionHandlerAdapter implements EventBus {
    private final EventRepository eventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final JsonSerde jsonSerde;
    private StompSession session;

    public DefaultEventBus(EventRepository eventRepository, KafkaTemplate<String, String> kafkaTemplate, JsonSerde jsonSerde) {
        this.eventRepository = eventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.jsonSerde = jsonSerde;
    }

    @Override
    public CompletableFuture<?> publish(Event event) {
        Assert.notNull(event, "event must not be null");
        if (eventRepository != null && TransactionSynchronizationManager.isActualTransactionActive()) {
            // 可以直接访问数据库的环境使用，也就是客户端和服务端处于同一个局域网内（Eventual Consistency）
            eventRepository.add(event);
            return CompletableFuture.completedFuture(null);
        } else if (kafkaTemplate != null) {
            // 可以直接访问 Kafka 的环境使用，也就是客户端和服务端处于同一个局域网内
            String payload = jsonSerde.serialize(event);
            return kafkaTemplate.send(event.eventDestination(), event.eventKey(), payload);
        } else if (session != null) {
            // 当调用方既没有数据库也没有 Kafka 时，说明客户端没有和服务端在同一个局域网内，
            // 此时客户端需要和服务端建立 WebSocket 连接
            String payload = jsonSerde.serialize(event);
            session.send(event.eventDestination(), payload);
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.failedFuture(new IllegalStateException("can not publish event"));
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.session = session;
    }
}
