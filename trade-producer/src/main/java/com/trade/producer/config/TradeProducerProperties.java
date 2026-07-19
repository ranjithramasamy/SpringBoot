package com.trade.producer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class TradeProducerProperties {
    private KafkaProperties kafka = new KafkaProperties();

    @Data
    public static class KafkaProperties {
        private String bootstrapServers;
        private String topicName;
        private ProducerProperties producer = new ProducerProperties();

        @Data
        public static class ProducerProperties {
            private String acks = "all";
            private int batchSize = 65536;
            private int lingerMs = 20;
            private String compressionType = "lz4";
        }
    }
}