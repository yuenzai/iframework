package cn.ecosync.iframework.autoconfigure.outbox;

import cn.ecosync.iframework.autoconfigure.outbox.debezium.DebeziumEngineConfiguration;
import cn.ecosync.iframework.outbox.bus.OutboxEventBus;
import cn.ecosync.iframework.outbox.repository.OutboxJpaRepository;
import cn.ecosync.iframework.serde.JsonSerde;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author yuenzai
 * @since 2024
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JpaRepository.class, OutboxJpaRepository.class})
@ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@EntityScan("cn.ecosync.iframework.outbox.domain")
@EnableJpaRepositories("cn.ecosync.iframework.outbox.repository")
@Import(DebeziumEngineConfiguration.class)
public class OutboxConfiguration {
    @Bean
    @ConditionalOnMissingBean(OutboxEventBus.class)
    public OutboxEventBus outboxEventBus(OutboxJpaRepository outboxJpaRepository, JsonSerde jsonSerde) {
        return new OutboxEventBus(outboxJpaRepository, jsonSerde);
    }
}
