package com.trade.producer.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.trade.producer.config.TradeProducerProperties;
import com.trade.producer.exception.CSVProcessingException;
import com.trade.producer.model.TradeRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeProducerService {

    private static final char BOM = '\uFEFF';

    private final Producer<String, TradeRecord> producer;
    private final TradeProducerProperties appProperties;

    public void processTradeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CSVProcessingException("Cannot process an empty file.");
        }

        try (Reader reader = stripBom(file.getInputStream())) {
            HeaderColumnNameMappingStrategy<TradeRecord> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(TradeRecord.class);

            CsvToBean<TradeRecord> csvToBean = new CsvToBeanBuilder<TradeRecord>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(true)
                    .build();

            List<TradeRecord> parsed = csvToBean.parse();
            log.info("Parsed {} raw rows from uploaded CSV", parsed.size());

            long sent = parsed.stream()
                    .filter(this::isValid)
                    .peek(this::sendToKafka)
                    .count();

            log.info("Sent {} valid records to Kafka ({} skipped as blank/invalid)",
                    sent, parsed.size() - sent);

        } catch (Exception e) {
            log.error("Failed to parse CSV file", e);
            throw new CSVProcessingException("Error parsing CSV: " + e.getMessage(), e);
        }
    }

    /** Wraps the upload stream and strips a leading UTF-8 BOM if present, so the
     *  first header column ("Ticker") matches its @CsvBindByName annotation. */
    private Reader stripBom(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        br.mark(1);
        int first = br.read();
        if (first != BOM) {
            br.reset();
        }
        return br;
    }

    /** Guards against blank/trailing lines producing all-null beans. */
    private boolean isValid(TradeRecord record) {
        boolean valid = StringUtils.isNotBlank(record.getTicker())
                && StringUtils.isNotBlank(record.getDate());
        if (!valid) {
            log.warn("Skipping blank/invalid row: {}", record);
        }
        return valid;
    }

    private void sendToKafka(TradeRecord record) {
        String topic = appProperties.getKafka().getTopicName();
        ProducerRecord<String, TradeRecord> kafkaRecord =
                new ProducerRecord<>(topic, record.getTicker(), record);
        log.info("Sending record: {}", record);
        producer.send(kafkaRecord, (metadata, ex) -> {
            if (ex != null) {
                log.error("Kafka delivery failed for ticker {}", record.getTicker(), ex);
            }
        });
    }
}