package com.trade.producer.controller;

import com.trade.producer.service.TradeProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeUploadController {

    private final TradeProducerService tradeProducerService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        tradeProducerService.processTradeFile(file);

        return ResponseEntity.ok("Trade file is being processed and sent to Kafka.");
    }
}