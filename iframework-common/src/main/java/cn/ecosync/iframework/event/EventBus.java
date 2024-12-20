package cn.ecosync.iframework.event;

import java.util.concurrent.CompletableFuture;

/**
 * @author yuenzai
 * @since 2024
 */
public interface EventBus {
    CompletableFuture<?> publish(Event event);
}
