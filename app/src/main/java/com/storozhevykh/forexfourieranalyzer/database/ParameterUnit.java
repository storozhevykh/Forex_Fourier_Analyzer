package com.storozhevykh.forexfourieranalyzer.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ParameterUnit {

    @PrimaryKey
    @NonNull
    private String name;
    private int intValue;
    private double doubleValue;

    public ParameterUnit(String name, int intValue, double doubleValue) {
        this.name = name;
        this.intValue = intValue;
        this.doubleValue = doubleValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }
}
