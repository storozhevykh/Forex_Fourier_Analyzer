package com.storozhevykh.forexfourieranalyzer.controller;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.database.DatabaseManager;
import com.storozhevykh.forexfourieranalyzer.database.ParameterUnit;
import com.storozhevykh.forexfourieranalyzer.view.BigMenuActivity;

import java.util.ArrayList;
import java.util.List;

public class ParametersHandler {

    private BigMenuActivity bigMenuActivity;

    private List<ParameterUnit> parameterUnitList;

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

    private final int numberOfModes_DEFAULT_VALUE = 2;
    private final int numberOfModes_MIN_VALUE = 1;
    private final int numberOfModes_MAX_VALUE = 10;
    private final int numberOfHarmonics_DEFAULT_VALUE = 3;
    private final int numberOfHarmonics_MIN_VALUE = 1;
    private final int numberOfHarmonics_MAX_VALUE = 10;
    private final int periodOfApproximation_DEFAULT_VALUE = 200;
    private final int periodOfApproximation_MIN_VALUE = 50;
    private final int predictedBars_DEFAULT_VALUE = 50;
    private final int predictedBars_MIN_VALUE = 10;
    private final double minPeriods_DEFAULT_VALUE = 0.5;
    private final double minPeriods_MIN_VALUE = 0.1;
    private final double minPeriods_MAX_VALUE = 4;
    private final double maxPeriods_DEFAULT_VALUE = 2;
    private final double maxPeriods_MAX_VALUE = 10;
    private final int periodStep_DEFAULT_VALUE = 10;
    private final int periodStep_MIN_VALUE = 1;
    private final int periodStep_MAX_VALUE = 50;
    private final int amplitudeSteps_DEFAULT_VALUE = 20;
    private final int amplitudeSteps_MIN_VALUE = 10;
    private final int amplitudeSteps_MAX_VALUE = 100;
    private final double phaseStep_DEFAULT_VALUE = 0.1;
    private final double phaseStep_MIN_VALUE = 0.01;
    private final double phaseStep_MAX_VALUE = 0.5;
    private final int highFreqFilter_DEFAULT_VALUE = 50;
    private final int highFreqFilter_MIN_VALUE = 10;
    private final int highFreqFilter_MAX_VALUE = 500;
    private final boolean dynamicApproximation_DEFAULT_VALUE = false;

    private final int selectedPairIndex_DEFAULT_VALUE = 0;
    private final int selectedTFIndex_DEFAULT_VALUE = 4;
    private final int maxBarsInHistory_DEFAULT_VALUE = 1000;
    private final int maxBarsInHistory_MIN_VALUE = 100;
    private final int maxBarsInHistory_MAX_VALUE = 100000;

    public ParametersHandler(BigMenuActivity bigMenuActivity) {
        this.bigMenuActivity = bigMenuActivity;
        parameterUnitList = new ArrayList<>();
        parameterUnitList = DatabaseManager.getParameters();
        if (parameterUnitList == null || parameterUnitList.size() == 0) {
            setDefaultValues();
            parameterUnitList = new ArrayList<>();
            makeParametersList();
            DatabaseManager.insertParametersList(parameterUnitList);
        }
        else {
            loadParameters();

        }
    }

    public void setDefaultValues() {
        numberOfModes = numberOfModes_DEFAULT_VALUE;
        numberOfHarmonics = numberOfHarmonics_DEFAULT_VALUE;
        periodOfApproximation = periodOfApproximation_DEFAULT_VALUE;
        predictedBars = predictedBars_DEFAULT_VALUE;
        minPeriods = minPeriods_DEFAULT_VALUE;
        maxPeriods = maxPeriods_DEFAULT_VALUE;
        periodStep = periodStep_DEFAULT_VALUE;
        amplitudeSteps = amplitudeSteps_DEFAULT_VALUE;
        phaseStep = phaseStep_DEFAULT_VALUE;
        highFreqFilter = highFreqFilter_DEFAULT_VALUE;
        dynamicApproximation = dynamicApproximation_DEFAULT_VALUE;
        selectedPairIndex = selectedPairIndex_DEFAULT_VALUE;
        selectedTFIndex = selectedTFIndex_DEFAULT_VALUE;
        maxBarsInHistory = maxBarsInHistory_DEFAULT_VALUE;
    }

    public void setFourierValuesFromEdit() {
        numberOfModes = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_number_of_modes)).getText().toString());
        numberOfHarmonics = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_number_of_harmonics)).getText().toString());
        periodOfApproximation = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_period_of_approximation)).getText().toString());
        predictedBars = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_predicted_bars)).getText().toString());
        minPeriods = Double.parseDouble(((EditText) bigMenuActivity.findViewById(R.id.edit_min_periods)).getText().toString());
        maxPeriods = Double.parseDouble(((EditText) bigMenuActivity.findViewById(R.id.edit_max_periods)).getText().toString());
        periodStep = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_period_step)).getText().toString());
        amplitudeSteps = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_amplitude_steps)).getText().toString());
        phaseStep = Double.parseDouble(((EditText) bigMenuActivity.findViewById(R.id.edit_phase_step)).getText().toString());
        highFreqFilter = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_high_freq_filter)).getText().toString());
        dynamicApproximation = ((CheckBox) bigMenuActivity.findViewById(R.id.dynamic_approximation_checkBox)).isChecked();

        makeParametersList();
        DatabaseManager.updateParametersList(parameterUnitList);
    }

    public void setChartValuesFromEdit() {
        selectedPairIndex = ((Spinner) bigMenuActivity.findViewById(R.id.choose_pair_spinner)).getSelectedItemPosition();
        selectedTFIndex = ((Spinner) bigMenuActivity.findViewById(R.id.choose_tf_spinner)).getSelectedItemPosition();
        maxBarsInHistory = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_bars_in_history)).getText().toString());

        makeParametersList();
        DatabaseManager.updateParametersList(parameterUnitList);
    }

    public void changeFourierValuesInEdit() {
        ((EditText) bigMenuActivity.findViewById(R.id.edit_number_of_modes)).setText(String.valueOf(numberOfModes));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_number_of_harmonics)).setText(String.valueOf(numberOfHarmonics));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_period_of_approximation)).setText(String.valueOf(periodOfApproximation));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_predicted_bars)).setText(String.valueOf(predictedBars));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_min_periods)).setText(String.valueOf(minPeriods));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_max_periods)).setText(String.valueOf(maxPeriods));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_period_step)).setText(String.valueOf(periodStep));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_amplitude_steps)).setText(String.valueOf(amplitudeSteps));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_phase_step)).setText(String.valueOf(phaseStep));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_high_freq_filter)).setText(String.valueOf(highFreqFilter));
        ((CheckBox) bigMenuActivity.findViewById(R.id.dynamic_approximation_checkBox)).setChecked(dynamicApproximation);
    }

    public void changeChartValuesInEdit() {
        ((Spinner) bigMenuActivity.findViewById(R.id.choose_pair_spinner)).setSelection(selectedPairIndex);
        ((Spinner) bigMenuActivity.findViewById(R.id.choose_tf_spinner)).setSelection(selectedTFIndex);
        ((EditText) bigMenuActivity.findViewById(R.id.edit_bars_in_history)).setText(String.valueOf(maxBarsInHistory));
    }

    private void loadParameters() {
        for (ParameterUnit parameterUnit: parameterUnitList) {
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

    private void makeParametersList() {
        parameterUnitList.clear();

        parameterUnitList.add(new ParameterUnit("numberOfModes", numberOfModes, 0));
        parameterUnitList.add(new ParameterUnit("numberOfHarmonics", numberOfHarmonics, 0));
        parameterUnitList.add(new ParameterUnit("periodOfApproximation", periodOfApproximation, 0));
        parameterUnitList.add(new ParameterUnit("predictedBars", predictedBars, 0));
        parameterUnitList.add(new ParameterUnit("minPeriods", 0, minPeriods));
        parameterUnitList.add(new ParameterUnit("maxPeriods", 0, maxPeriods));
        parameterUnitList.add(new ParameterUnit("periodStep", periodStep, 0));
        parameterUnitList.add(new ParameterUnit("amplitudeSteps", amplitudeSteps, 0));
        parameterUnitList.add(new ParameterUnit("phaseStep", 0, phaseStep));
        parameterUnitList.add(new ParameterUnit("highFreqFilter", highFreqFilter, 0));
        if (dynamicApproximation)
            parameterUnitList.add(new ParameterUnit("dynamicApproximation", 1, 0));
        else
            parameterUnitList.add(new ParameterUnit("dynamicApproximation", 0, 0));

        parameterUnitList.add(new ParameterUnit("selectedPairIndex", selectedPairIndex, 0));
        parameterUnitList.add(new ParameterUnit("selectedTFIndex", selectedTFIndex, 0));
        parameterUnitList.add(new ParameterUnit("maxBarsInHistory", maxBarsInHistory, 0));
    }

    public boolean checkFourierParameters() {
        Log.i("check", "checkFourierParameters");
        setFourierValuesFromEdit();
        boolean result = true;
        if (numberOfModes < numberOfModes_MIN_VALUE || numberOfModes > numberOfModes_MAX_VALUE) {
            bigMenuActivity.findViewById(R.id.btn_error_number_of_modes).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_number_of_modes).setVisibility(View.INVISIBLE);

        if (numberOfHarmonics < numberOfHarmonics_MIN_VALUE || numberOfHarmonics > numberOfHarmonics_MAX_VALUE) {
            bigMenuActivity.findViewById(R.id.btn_error_number_of_harmonics).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_number_of_harmonics).setVisibility(View.INVISIBLE);

        if (periodOfApproximation < periodOfApproximation_MIN_VALUE || periodOfApproximation > maxBarsInHistory) {
            bigMenuActivity.findViewById(R.id.btn_error_period_of_approximation).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_period_of_approximation).setVisibility(View.INVISIBLE);

        if (predictedBars < predictedBars_MIN_VALUE || predictedBars > periodOfApproximation) {
            bigMenuActivity.findViewById(R.id.btn_error_predicted_bars).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_predicted_bars).setVisibility(View.INVISIBLE);

        if (minPeriods < minPeriods_MIN_VALUE || minPeriods > minPeriods_MAX_VALUE || minPeriods > maxPeriods) {
            bigMenuActivity.findViewById(R.id.btn_error_min_periods).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_min_periods).setVisibility(View.INVISIBLE);

        if (maxPeriods > maxPeriods_MAX_VALUE || minPeriods > maxPeriods) {
            bigMenuActivity.findViewById(R.id.btn_error_min_periods).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_min_periods).setVisibility(View.INVISIBLE);

        if (periodStep < periodStep_MIN_VALUE || periodStep > periodStep_MAX_VALUE) {
            bigMenuActivity.findViewById(R.id.btn_error_period_step).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_period_step).setVisibility(View.INVISIBLE);

        if (amplitudeSteps < amplitudeSteps_MIN_VALUE || amplitudeSteps > amplitudeSteps_MAX_VALUE) {
            bigMenuActivity.findViewById(R.id.btn_error_amplitude_steps).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_amplitude_steps).setVisibility(View.INVISIBLE);

        if (phaseStep < phaseStep_MIN_VALUE || phaseStep > phaseStep_MAX_VALUE) {
            bigMenuActivity.findViewById(R.id.btn_error_phase_step).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_phase_step).setVisibility(View.INVISIBLE);

        if (highFreqFilter < highFreqFilter_MIN_VALUE || highFreqFilter > highFreqFilter_MAX_VALUE) {
            bigMenuActivity.findViewById(R.id.btn_error_high_freq_filter).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_high_freq_filter).setVisibility(View.INVISIBLE);

        return result;
    }

    public boolean checkChartParameters() {
        Log.i("check", "checkChartParameters");
        setChartValuesFromEdit();
        boolean result = true;
        if (maxBarsInHistory < maxBarsInHistory_MIN_VALUE || maxBarsInHistory > maxBarsInHistory_MAX_VALUE || maxBarsInHistory < periodOfApproximation) {
            bigMenuActivity.findViewById(R.id.btn_error_bars_in_history).setVisibility(View.VISIBLE);
            result = false;
        }
        else
            bigMenuActivity.findViewById(R.id.btn_error_bars_in_history).setVisibility(View.INVISIBLE);

        return result;
    }

    public List<ParameterUnit> getParameterUnitList() {
        return parameterUnitList;
    }

}




