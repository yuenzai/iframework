//package cn.ecosync.iframework.outbox.model;
//
//import cn.ecosync.iframework.domain.IdentifiedValueObject;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Table;
//
///**
// * @author yuenzai
// * @since 2024
// */
//@Entity
//@Table(name = "outbox_processed")
//public class OutboxProcessed extends IdentifiedValueObject {
//    /**
//     * 事件唯一标识
//     */
//    @Column(name = "event_id", nullable = false, updatable = false, unique = true)
//    private String eventId;
//
//    public OutboxProcessed() {
//    }
//
//    public OutboxProcessed(String eventId) {
//        this.eventId = eventId;
//    }
//}
