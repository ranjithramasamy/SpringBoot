package com.abc.sid.notifsrvc.app.messaging;

import com.abc.sid.notifsrvc.engine.orchestrator.XMLTransformationOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.kafka.consumer.enabled", havingValue = "true", matchIfMissing = true)
public class CorporateActionKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(CorporateActionKafkaConsumer.class);

    private final XMLTransformationOrchestrator orchestrator;

    public CorporateActionKafkaConsumer(XMLTransformationOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @KafkaListener(
            topics = "${app.kafka.topics.incoming}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onMessage(String payload, Acknowledgment acknowledgment) {
        log.debug("Received incoming corporate action message from Kafka");
        orchestrator.orchestrate(payload);
        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
    }
}
