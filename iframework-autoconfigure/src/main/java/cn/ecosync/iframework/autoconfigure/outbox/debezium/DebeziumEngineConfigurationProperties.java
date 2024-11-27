package cn.ecosync.iframework.autoconfigure.outbox.debezium;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @author yuenzai
 * @since 2024
 */
@ConfigurationProperties("outbox.debezium")
public class DebeziumEngineConfigurationProperties {
    @NotEmpty
    private String databaseHostname;
    @NotEmpty
    private String databasePort;
    @NotEmpty
    private String schemaName;
    @NotEmpty
    private String databaseUsername;
    @NotEmpty
    private String databasePassword;
    @NotEmpty
    private String bootstrapServers;

    public String getDatabaseHostname() {
        return databaseHostname;
    }

    public void setDatabaseHostname(String databaseHostname) {
        this.databaseHostname = databaseHostname;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Properties toProperties() {
        Properties props = new Properties();
        props.setProperty("name", "debezium-mysql");
        props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        // OffsetBackingStore properties
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.KafkaOffsetBackingStore");
        props.setProperty("offset.storage.topic", "mysql-debezium-offsets");
        props.setProperty("offset.storage.partitions", "1");
        props.setProperty("offset.storage.replication.factor", "1");
        // Engine Properties
        props.setProperty("bootstrap.servers", bootstrapServers);
        // Connector properties
        props.setProperty("database.hostname", databaseHostname);
        props.setProperty("database.port", databasePort);
        props.setProperty("database.user", databaseUsername);
        props.setProperty("database.password", databasePassword);
        props.setProperty("database.server.id", "85744");
        props.setProperty("database.include.list", schemaName);
        props.setProperty("table.include.list", schemaName + "\\.outbox");
        props.setProperty("topic.prefix", "mysql");
        props.setProperty("topic.delimiter", "-");
        // predicates properties
        props.setProperty("predicates", "isOutboxEvent");
        props.setProperty("predicates.isOutboxEvent.type", "org.apache.kafka.connect.transforms.predicates.TopicNameMatches");
        props.setProperty("predicates.isOutboxEvent.pattern", String.format("mysql-%s-outbox", schemaName));
        // transforms properties
        props.setProperty("transforms", "outbox");
        props.setProperty("transforms.outbox.type", "io.debezium.transforms.outbox.EventRouter");
        props.setProperty("transforms.outbox.route.by.field", "aggregate_type");
        props.setProperty("transforms.outbox.route.topic.replacement", String.format("%s-${routedByValue}", schemaName));
        props.setProperty("transforms.outbox.table.fields.additional.placement", "event_type:header:eventType,event_time:header:eventTime");
        props.setProperty("transforms.outbox.table.field.event.id", "event_id");
        props.setProperty("transforms.outbox.table.field.event.key", "aggregate_id");
        props.setProperty("transforms.outbox.table.field.event.payload", "payload");
        props.setProperty("transforms.outbox.predicate", "isOutboxEvent");
        // Database schema history properties
        props.setProperty("schema.history.internal", "io.debezium.storage.kafka.history.KafkaSchemaHistory");
        props.setProperty("schema.history.internal.kafka.topic", "mysql-schema-history");
        props.setProperty("schema.history.internal.kafka.bootstrap.servers", bootstrapServers);
        return props;
    }
}
