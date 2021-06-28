package com.storozhevykh.forexfourieranalyzer.quotes;

public class Quote {

    private String datetime;
    private double open;
    private double high;
    private double low;
    private double close;

    public Quote(String datetime, double open, double high, double low, double close) {
        this.datetime = datetime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public String getDatetime() {
        return datetime;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }
}
