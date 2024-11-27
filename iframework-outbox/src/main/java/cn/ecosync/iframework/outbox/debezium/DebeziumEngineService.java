package cn.ecosync.iframework.outbox.debezium;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.debezium.engine.format.SerializationFormat;
import io.debezium.engine.format.SimpleString;
import io.debezium.engine.spi.OffsetCommitPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author yuenzai
 * @since 2024
 */
public class DebeziumEngineService<T> implements DebeziumEngine.CompletionCallback, SmartLifecycle {
    private static final Logger log = LoggerFactory.getLogger(DebeziumEngineService.class);

    private final DebeziumEngine<ChangeEvent<String, T>> debeziumEngine;
    private final Executor executor;

    private volatile boolean running = false;

    public DebeziumEngineService(Class<? extends SerializationFormat<T>> valueFormat, Properties properties,
                                 DebeziumEngine.ChangeConsumer<ChangeEvent<String, T>> consumer) {
        this(valueFormat, properties, consumer, Executors.newSingleThreadExecutor());
    }

    public DebeziumEngineService(Class<? extends SerializationFormat<T>> valueFormat, Properties properties,
                                 DebeziumEngine.ChangeConsumer<ChangeEvent<String, T>> consumer,
                                 Executor executor) {
        Assert.notNull(executor, "executor must not be null");
        Assert.notNull(consumer, "consumer must not be null");
        this.debeziumEngine = DebeziumEngine.create(SimpleString.class, valueFormat, Json.class)
                .using(properties)
                .using(OffsetCommitPolicy.always())
                .using(new SimpleLoggingCallback())
                .using(this)
                .notifying(consumer)
                .build();
        this.executor = executor;
    }

    @Override
    public void start() {
        executor.execute(debeziumEngine);
        running = true;
    }

    @Override
    public void stop() {
        try {
            debeziumEngine.close();
        } catch (IOException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void handle(boolean success, String message, Throwable error) {
        running = false;
        if (success) {
            log.info(message);
        } else {
            log.error(message, error);
        }
    }

    private static class SimpleLoggingCallback implements DebeziumEngine.ConnectorCallback {
        @Override
        public void connectorStarted() {
            log.info("connector started");
        }

        @Override
        public void connectorStopped() {
            log.info("connector stopped");
        }

        @Override
        public void taskStarted() {
            log.info("task started");
        }

        @Override
        public void taskStopped() {
            log.info("task stopped");
        }
    }
}
