package com.storozhevykh.forexfourieranalyzer.controller;

public class MoreSpinnerObject {

    private String title;
    private boolean selected;

    public MoreSpinnerObject(String title, boolean selected) {
        this.title = title;
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
