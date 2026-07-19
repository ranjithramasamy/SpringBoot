package com.trade.producer.exception;

public class CSVProcessingException extends RuntimeException {
    public CSVProcessingException(String message) {
        super(message);
    }

    public CSVProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}