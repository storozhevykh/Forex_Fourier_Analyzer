package com.storozhevykh.forexfourieranalyzer.controller;

import com.storozhevykh.forexfourieranalyzer.database.ParameterUnit;

import java.lang.reflect.Parameter;
import java.util.List;

public class ParametersOwner {

    private static ParametersOwner instance;

    private int numberOfModes;
    private int numberOfHarmonics;
    private int periodOfApproximation;
    private int predictedBars;
    private double minPeriods;
    private double maxPeriods;
    private int periodStep;
    private int amplitudeSteps;
    private double phaseStep;
    private int highFreqFilter;
    private boolean dynamicApproximation;

    private int selectedPairIndex;
    private int selectedTFIndex;
    private int maxBarsInHistory;

    private ParametersOwner() {
    }

    public static ParametersOwner getInstance() {
        if (instance == null)
            instance = new ParametersOwner();
        return instance;
    }

    public void setParameters (List<ParameterUnit> parameterUnits) {
        for (ParameterUnit parameterUnit: parameterUnits) {
            switch (parameterUnit.getName()) {
                case "numberOfModes":
                    numberOfModes = parameterUnit.getIntValue();
                    break;
                case "numberOfHarmonics":
                    numberOfHarmonics = parameterUnit.getIntValue();
                    break;
                case "periodOfApproximation":
                    periodOfApproximation = parameterUnit.getIntValue();
                    break;
                case "predictedBars":
                    predictedBars = parameterUnit.getIntValue();
                    break;
                case "minPeriods":
                    minPeriods = parameterUnit.getDoubleValue();
                    break;
                case "maxPeriods":
                    maxPeriods = parameterUnit.getDoubleValue();
                    break;
                case "periodStep":
                    periodStep = parameterUnit.getIntValue();
                    break;
                case "amplitudeSteps":
                    amplitudeSteps = parameterUnit.getIntValue();
                    break;
                case "phaseStep":
                    phaseStep = parameterUnit.getDoubleValue();
                    break;
                case "highFreqFilter":
                    highFreqFilter = parameterUnit.getIntValue();
                    break;
                case "dynamicApproximation":
                    dynamicApproximation = parameterUnit.getIntValue() != 0;
                    break;
                case "selectedPairIndex":
                    selectedPairIndex = parameterUnit.getIntValue();
                    break;
                case "selectedTFIndex":
                    selectedTFIndex = parameterUnit.getIntValue();
                    break;
                case "maxBarsInHistory":
                    maxBarsInHistory = parameterUnit.getIntValue();
                    break;
            }
        }
    }

    public int getNumberOfModes() {
        return numberOfModes;
    }

    public int getNumberOfHarmonics() {
        return numberOfHarmonics;
    }

    public int getPeriodOfApproximation() {
        return periodOfApproximation;
    }

    public int getPredictedBars() {
        return predictedBars;
    }

    public double getMinPeriods() {
        return minPeriods;
    }

    public double getMaxPeriods() {
        return maxPeriods;
    }

    public int getPeriodStep() {
        return periodStep;
    }

    public int getAmplitudeSteps() {
        return amplitudeSteps;
    }

    public double getPhaseStep() {
        return phaseStep;
    }

    public int getHighFreqFilter() {
        return highFreqFilter;
    }

    public boolean isDynamicApproximation() {
        return dynamicApproximation;
    }

    public int getSelectedPairIndex() {
        return selectedPairIndex;
    }

    public int getSelectedTFIndex() {
        return selectedTFIndex;
    }

    public int getMaxBarsInHistory() {
        return maxBarsInHistory;
    }
}
