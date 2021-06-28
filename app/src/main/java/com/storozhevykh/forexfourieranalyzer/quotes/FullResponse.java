package com.storozhevykh.forexfourieranalyzer.quotes;

import java.util.List;

public class FullResponse {
    private Meta meta;
    private List<Quote> values;

    public FullResponse(Meta meta, List<Quote> values) {
        this.meta = meta;
        this.values = values;
    }

    public Meta getMeta() {
        return meta;
    }

    public List<Quote> getValues() {
        return values;
    }
}
