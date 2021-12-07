package com.storozhevykh.forexfourieranalyzer.quotes;

import java.util.List;

public class BarItem {

    private String datetime;
    private int open;
    private int high;
    private int low;
    private int close;
    private int fourier;
    private int baseLine;
    private List<Integer> harmonics;

    public BarItem(String datetime, int open, int high, int low, int close, int fourier, int baseLine, List<Integer> harmonics) {
        this.datetime = datetime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.fourier = fourier;
        this.baseLine = baseLine;
        this.harmonics = harmonics;
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

    public int getBaseLine() {
        return baseLine;
    }

    public void setFourier(int fourier) {
        this.fourier = fourier;
    }

    public void setBaseLine(int baseLine) {
        this.baseLine = baseLine;
    }

    public List<Integer> getHarmonics() {
        return harmonics;
    }

    public void setHarmonics(List<Integer> harmonics) {
        this.harmonics = harmonics;
    }
}
