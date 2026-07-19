package com.trade.producer.config;

import com.trade.producer.model.TradeRecord;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaNativeConfig {

    private final TradeProducerProperties appProperties;

    @Bean
    public Producer<String, TradeRecord> nativeKafkaProducer() {
        TradeProducerProperties.KafkaProperties kafka = appProperties.getKafka();
        TradeProducerProperties.KafkaProperties.ProducerProperties p = kafka.getProducer();

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class.getName());
        props.put(JacksonJsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        props.put(ProducerConfig.ACKS_CONFIG, p.getAcks());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, p.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, p.getLingerMs());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, p.getCompressionType());

        // Meaningful once you have real broker failover to test against:
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);       // avoids dup writes on retry across broker restarts
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5); // safe max with idempotence on
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);   // per-request timeout
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000); // >= linger.ms + request.timeout.ms

        return new KafkaProducer<>(props);
    }
}