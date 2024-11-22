package cn.ecosync.iframework.domain;

/**
 * @author yuenzai
 * @since 2024
 */
public interface AggregateRoot {
    String aggregateType();

    String aggregateId();
}
