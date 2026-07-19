package com.trade.producer.service;

import com.trade.producer.config.TradeProducerProperties;
import com.trade.producer.model.TradeRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeKafkaProducer {

    private final Producer<String, TradeRecord> producer;
    private final TradeProducerProperties appProperties;

    public void sendTrade(TradeRecord trade) {
        String topic = appProperties.getKafka().getTopicName();
        ProducerRecord<String, TradeRecord> record =
                new ProducerRecord<>(topic, trade.getTicker(), trade);

        producer.send(record, (metadata, ex) -> {
            if (ex != null) log.error("Kafka delivery failed", ex);
        });
    }
}