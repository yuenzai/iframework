package cn.ecosync.iframework.autoconfigure;

import cn.ecosync.iframework.bus.DefaultCommandBus;
import cn.ecosync.iframework.bus.DefaultEventBus;
import cn.ecosync.iframework.bus.DefaultQueryBus;
import cn.ecosync.iframework.bus.OutboxEventBus;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.EventBus;
import cn.ecosync.iframework.jpa.repository.OutboxJpaRepository;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.query.QueryHandler;
import cn.ecosync.iframework.serde.JacksonSerde;
import cn.ecosync.iframework.serde.JsonSerde;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * @author yuenzai
 * @since 2024
 */
@AutoConfiguration
public class IFrameworkAutoConfiguration {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(DefaultCommandBus.class)
    @ConditionalOnMissingBean(CommandBus.class)
    public static class DefaultCommandBusConfiguration {
        @Bean
        public DefaultCommandBus defaultCommandBus(List<CommandHandler<?>> commandHandlers) {
            DefaultCommandBus defaultCommandBus = new DefaultCommandBus(commandHandlers);
            defaultCommandBus.logging();
            return defaultCommandBus;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(DefaultQueryBus.class)
    @ConditionalOnMissingBean(QueryBus.class)
    public static class DefaultQueryBusConfiguration {
        @Bean
        public DefaultQueryBus defaultQueryBus(List<QueryHandler<?, ?>> queryHandlers) {
            DefaultQueryBus defaultQueryBus = new DefaultQueryBus(queryHandlers);
            defaultQueryBus.logging();
            return defaultQueryBus;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(DefaultEventBus.class)
    @ConditionalOnMissingBean(EventBus.class)
    public static class DefaultEventBusConfiguration {
        @Bean
        public DefaultEventBus defaultEventBus() {
            return new DefaultEventBus();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ObjectMapper.class, JacksonSerde.class})
    @ConditionalOnMissingBean(JsonSerde.class)
    public static class JsonSerdeConfiguration {
        @Bean
        public JacksonSerde jsonSerde(ObjectMapper objectMapper) {
            return new JacksonSerde(objectMapper);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({JpaRepository.class, OutboxJpaRepository.class})
    @ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
    @EntityScan("cn.ecosync.iframework.outbox")
    @EnableJpaRepositories("cn.ecosync.iframework.jpa.repository")
    public static class OutboxConfiguration {
        @Bean
        public OutboxEventBus outboxEventBus(OutboxJpaRepository outboxJpaRepository, JsonSerde jsonSerde) {
            return new OutboxEventBus(outboxJpaRepository, jsonSerde);
        }
    }
}
