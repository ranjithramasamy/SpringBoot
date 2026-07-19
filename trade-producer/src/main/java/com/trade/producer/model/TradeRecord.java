package com.trade.producer.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class TradeRecord {
    @CsvBindByName(column = "Ticker")
    private String ticker;

    @CsvBindByName(column = "Date")
    private String date;

    @CsvBindByName(column = "Open")
    private String open;

    @CsvBindByName(column = "High")
    private String high;

    @CsvBindByName(column = "Low")
    private String low;

    @CsvBindByName(column = "Close")
    private String close;

    @CsvBindByName(column = "Volume")
    private String volume;

    @CsvBindByName(column = "Dividends")
    private String dividends;

    @CsvBindByName(column = "Stock Splits")
    private String stockSplits;
}