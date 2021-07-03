package com.storozhevykh.forexfourieranalyzer.quotes;

public class BarItem {

    private String datetime;
    private int open;
    private int high;
    private int low;
    private int close;
    private int fourier;

    public BarItem(String datetime, int open, int high, int low, int close, int fourier) {
        this.datetime = datetime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.fourier = fourier;
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

    public int getFourier() {
        return fourier;
    }

    public void setFourier(int fourier) {
        this.fourier = fourier;
    }
}
