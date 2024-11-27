package cn.ecosync.iframework.outbox.debezium;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.Header;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author yuenzai
 * @since 2024
 */
public class ChangeEventKafkaConsumer implements DebeziumEngine.ChangeConsumer<ChangeEvent<String, String>> {
    private static final Logger log = LoggerFactory.getLogger(ChangeEventKafkaConsumer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Predicate<ChangeEvent<?, ?>> eventPredicate;

    public ChangeEventKafkaConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this(kafkaTemplate, in -> true);
    }

    public ChangeEventKafkaConsumer(KafkaTemplate<String, String> kafkaTemplate, Predicate<ChangeEvent<?, ?>> eventPredicate) {
        Assert.notNull(kafkaTemplate, "kafkaTemplate must not be null");
        Assert.notNull(eventPredicate, "eventPredicate must not be null");
        this.kafkaTemplate = kafkaTemplate;
        this.eventPredicate = eventPredicate;
    }

    @Override
    public void handleBatch(List<ChangeEvent<String, String>> records, DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) {
        List<CompletableFuture<ChangeEvent<String, String>>> futures = records.stream()
                .peek(in -> log.debug("{}", in))
                .map(this::handle)
                .collect(Collectors.toList());

        try {
            List<ChangeEvent<String, String>> changeEvents = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                    .handle((ignored, e) -> {
                        if (e != null) {
                            log.error("", e);
                        }
                        List<ChangeEvent<String, String>> list = new ArrayList<>();
                        for (CompletableFuture<ChangeEvent<String, String>> future : futures) {
                            if (future.isCompletedExceptionally()) {
                                break;
                            }
                            ChangeEvent<String, String> record = future.join();
                            list.add(record);
                        }
                        return list;
                    })
                    .get();
            changeEvents.forEach(in -> markProcessed(in, committer));
        } catch (Exception e) {
            log.error("", e);
        } finally {
            markBatchFinished(committer);
        }
    }

    private CompletableFuture<ChangeEvent<String, String>> handle(ChangeEvent<String, String> record) {
        try {
            if (!eventPredicate.test(record)) {
                return CompletableFuture.completedFuture(record);
            }
            ProducerRecord<String, String> producerRecord = map(record);
            return kafkaTemplate.send(producerRecord)
                    .thenApply(in -> record);
        } catch (Exception e) {
            log.error("", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * todo producer block
     * buffer.memory
     * max.block.ms
     */
    private ProducerRecord<String, String> map(ChangeEvent<String, String> event) {
        Assert.notNull(event, "event must not be null");
        String topic = event.destination();
        String key = event.key();
        String value = event.value();
        Map<String, String> headerMap = event.headers().stream()
                .collect(Collectors.toMap(Header::getKey, in -> (String) in.getValue()));
        String eventId = headerMap.get("id");
        String eventType = headerMap.get("eventType");
        String eventTime = headerMap.get("eventTime");
        RecordHeaders headers = new RecordHeaders();
        headers.add("eventId", eventId.replaceAll("\"", "").getBytes(StandardCharsets.UTF_8));
        headers.add("eventType", eventType.replaceAll("\"", "").getBytes(StandardCharsets.UTF_8));
        headers.add("eventTime", eventTime.getBytes(StandardCharsets.UTF_8));
        // send message to kafka
        return new ProducerRecord<>(topic, null, null, key, value, headers);
    }

    private static void markProcessed(ChangeEvent<String, String> record, DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) {
        try {
            committer.markProcessed(record);
            log.info("markProcessed: topic: {}, key: {}", record.destination(), record.key());
        } catch (Exception e) {
            log.error("exception occurred on markProcessed", e);
        }
    }

    private static <R> void markBatchFinished(DebeziumEngine.RecordCommitter<R> committer) {
        try {
            committer.markBatchFinished();
            log.info("markBatchFinished");
        } catch (Exception e) {
            log.error("exception occurred on markBatchFinished", e);
        }
    }
}
