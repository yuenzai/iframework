package cn.ecosync.iframework.event;

/**
 * @author yuenzai
 * @since 2024
 */
public interface EventBus {
    void publish(Event event);
}
