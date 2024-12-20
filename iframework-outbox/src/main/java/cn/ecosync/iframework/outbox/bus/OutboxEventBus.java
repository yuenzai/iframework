//package cn.ecosync.iframework.outbox.bus;
//
//import cn.ecosync.iframework.event.AbstractEventBus;
//import cn.ecosync.iframework.event.Event;
//import cn.ecosync.iframework.event.repository.EventRepository;
//import cn.ecosync.iframework.outbox.model.OutboxEvent;
//import org.springframework.util.Assert;
//
///**
// * @author yuenzai
// * @since 2024
// */
//public class OutboxEventBus extends AbstractEventBus {
//    private final EventRepository eventRepository;
//
//    public OutboxEventBus(EventRepository eventRepository) {
//        this.eventRepository = eventRepository;
//    }
//
//    @Override
//    public void publish(Event event) {
//        Assert.notNull(event, "event must not be null");
//        eventRepository.add(new OutboxEvent(event));
//    }
//}
