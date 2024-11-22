package cn.ecosync.iframework.bus;

import cn.ecosync.iframework.event.AbstractEventBus;
import cn.ecosync.iframework.event.Event;

/**
 * @author yuenzai
 * @since 2024
 */
public class DefaultEventBus extends AbstractEventBus {
    @Override
    public void publish(Event event) {
        handle(event);
    }
}
