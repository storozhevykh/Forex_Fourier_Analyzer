package com.storozhevykh.forexfourieranalyzer.quotes;

public class BarItem {

    private String datetime;
    private int open;
    private int high;
    private int low;
    private int close;

    public BarItem(String datetime, int open, int high, int low, int close) {
        this.datetime = datetime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getOpen() {
        return open;
    }

    public int getHigh() {
        return high;
    }

    public int getLow() {
        return low;
    }

    public int getClose() {
        return close;
    }
}
