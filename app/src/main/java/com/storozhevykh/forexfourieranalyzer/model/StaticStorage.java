package com.storozhevykh.forexfourieranalyzer.model;

import java.util.ArrayList;
import java.util.List;

public class StaticStorage {
    public static List<Double> calcApproximationList;
    public static List<Double> calcBaseLineList;
    public static List<Double> calcPredictionList;
    public static List<Integer> predictionList;

    public static void init() {
        calcApproximationList = null;
        calcBaseLineList = null;
        calcPredictionList = new ArrayList<>();
        predictionList = new ArrayList<>();
    }
}
