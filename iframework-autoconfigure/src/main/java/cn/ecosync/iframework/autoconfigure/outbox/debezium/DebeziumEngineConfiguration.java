package cn.ecosync.iframework.autoconfigure.outbox.debezium;

import cn.ecosync.iframework.outbox.debezium.ChangeEventKafkaConsumer;
import cn.ecosync.iframework.outbox.debezium.DebeziumEngineService;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine.ChangeConsumer;
import io.debezium.engine.format.Json;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Properties;
import java.util.function.Predicate;

/**
 * @author yuenzai
 * @since 2024
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({EmbeddedEngine.class, KafkaTemplate.class})
@EnableConfigurationProperties(DebeziumEngineConfigurationProperties.class)
public class DebeziumEngineConfiguration {
    private final DebeziumEngineConfigurationProperties debeziumEngineProperties;

    public DebeziumEngineConfiguration(DebeziumEngineConfigurationProperties debeziumEngineProperties) {
        this.debeziumEngineProperties = debeziumEngineProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public DebeziumEngineService<String> debeziumEngineService(ChangeConsumer<ChangeEvent<String, String>> consumer) {
        Properties properties = debeziumEngineProperties.toProperties();
        properties.setProperty("transforms.outbox.table.expand.json.payload", "true");
        properties.setProperty("converter.schemas.enable", "false");
        return new DebeziumEngineService<>(Json.class, properties, consumer);
    }

    @Bean
    @ConditionalOnMissingBean
    public ChangeConsumer<ChangeEvent<String, String>> debeziumEngineConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        String schemaName = debeziumEngineProperties.getSchemaName();
        Predicate<ChangeEvent<?, ?>> changeEventPredicate = in -> in.destination().startsWith(schemaName);
        return new ChangeEventKafkaConsumer(kafkaTemplate, changeEventPredicate);
    }
}
