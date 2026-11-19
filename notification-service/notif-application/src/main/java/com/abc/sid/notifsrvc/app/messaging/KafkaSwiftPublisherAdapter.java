package com.abc.sid.notifsrvc.app.messaging;

import com.abc.sid.notifsrvc.common.exception.MessagePublishException;
import com.abc.sid.notifsrvc.common.util.SensitiveDataMasker;
import com.abc.sid.notifsrvc.domain.port.SwiftPublisherPort;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class KafkaSwiftPublisherAdapter implements SwiftPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaSwiftPublisherAdapter.class);

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String outgoingTopic;
    private final boolean publishEnabled;

    public KafkaSwiftPublisherAdapter(
            @Autowired(required = false) KafkaTemplate<String, byte[]> kafkaTemplate,
            @Value("${app.kafka.topics.outgoing:corp-actions.outgoing.swift.v1}") String outgoingTopic,
            @Value("${app.kafka.producer.enabled:true}") boolean publishEnabled) {
        this.kafkaTemplate = kafkaTemplate;
        this.outgoingTopic = outgoingTopic;
        this.publishEnabled = publishEnabled;
    }

    @Override
    public void publish(String partitionKey, byte[] xmlPayload, Map<String, String> headers) {
        if (!publishEnabled || kafkaTemplate == null) {
            log.info("Kafka publish disabled (dry-run mode). Key={}, MaskedXML={}",
                    partitionKey,
                    SensitiveDataMasker.maskXmlForLogging(new String(xmlPayload, StandardCharsets.UTF_8)));
            return;
        }
        try {
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(outgoingTopic, partitionKey, xmlPayload);
            headers.forEach((k, v) -> record.headers().add(k, v.getBytes(StandardCharsets.UTF_8)));
            kafkaTemplate.send(record).get();
        } catch (Exception e) {
            throw new MessagePublishException("Failed to publish ISO 20022 XML to topic " + outgoingTopic, e);
        }
    }
}
