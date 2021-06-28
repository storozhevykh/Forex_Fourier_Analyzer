package com.storozhevykh.forexfourieranalyzer.quotes;

public class Meta {

    private String symbol;
    private String interval;
    private String currency_base;
    private String currency_quote;
    private String type;

    public Meta(String symbol, String interval, String currency_base, String currency_quote, String type) {
        this.symbol = symbol;
        this.interval = interval;
        this.currency_base = currency_base;
        this.currency_quote = currency_quote;
        this.type = type;
    }
}
