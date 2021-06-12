package com.storozhevykh.forexfourieranalyzer.controller;

import android.widget.CheckBox;
import android.widget.EditText;

import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.database.DatabaseManager;
import com.storozhevykh.forexfourieranalyzer.database.ParameterUnit;
import com.storozhevykh.forexfourieranalyzer.view.BigMenuActivity;

import java.util.ArrayList;
import java.util.List;

public class ParametersHandler {

    private BigMenuActivity bigMenuActivity;

    private List<ParameterUnit> parameterUnitList;

    private EditText edit_NumberOfModes;
    private EditText edit_NumberOfHarmonics;
    private EditText edit_PeriodOfApproximation;
    private EditText edit_PredictedBars;
    private EditText edit_MinPeriods;
    private EditText edit_MaxPeriods;
    private EditText edit_PeriodStep;
    private EditText edit_AmplitudeSteps;
    private EditText edit_PhaseStep;
    private EditText edit_HighFreqFilter;
    private EditText edit_DynamicApproximation;

    private int NumberOfModes;
    private int NumberOfHarmonics;
    private int PeriodOfApproximation;
    private int PredictedBars;
    private double MinPeriods;
    private double MaxPeriods;
    private int PeriodStep;
    private int AmplitudeSteps;
    private double PhaseStep;
    private int HighFreqFilter;
    private boolean DynamicApproximation;

    private final int NumberOfModes_DEFAUL_VALUE = 2;
    private final int NumberOfHarmonics_DEFAUL_VALUE = 3;
    private final int PeriodOfApproximation_DEFAUL_VALUE = 200;
    private final int PredictedBars_DEFAUL_VALUE = 50;
    private final double MinPeriods_DEFAUL_VALUE = 0.5;
    private final double MaxPeriods_DEFAUL_VALUE = 2;
    private final int PeriodStep_DEFAUL_VALUE = 10;
    private final int AmplitudeSteps_DEFAUL_VALUE = 20;
    private final double PhaseStep_DEFAUL_VALUE = 0.1;
    private final int HighFreqFilter_DEFAUL_VALUE = 50;
    private final boolean DynamicApproximation_DEFAUL_VALUE = false;

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
        NumberOfModes = NumberOfModes_DEFAUL_VALUE;
        NumberOfHarmonics = NumberOfHarmonics_DEFAUL_VALUE;
        PeriodOfApproximation = PeriodOfApproximation_DEFAUL_VALUE;
        PredictedBars = PredictedBars_DEFAUL_VALUE;
        MinPeriods = MinPeriods_DEFAUL_VALUE;
        MaxPeriods = MaxPeriods_DEFAUL_VALUE;
        PeriodStep = PeriodStep_DEFAUL_VALUE;
        AmplitudeSteps = AmplitudeSteps_DEFAUL_VALUE;
        PhaseStep = PhaseStep_DEFAUL_VALUE;
        HighFreqFilter = HighFreqFilter_DEFAUL_VALUE;
        DynamicApproximation = DynamicApproximation_DEFAUL_VALUE;
    }

    public void setValuesFromEdit() {
        NumberOfModes = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_number_of_modes)).getText().toString());
        NumberOfHarmonics = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_number_of_harmonics)).getText().toString());
        PeriodOfApproximation = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_period_of_approximation)).getText().toString());
        PredictedBars = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_predicted_bars)).getText().toString());
        MinPeriods = Double.parseDouble(((EditText) bigMenuActivity.findViewById(R.id.edit_min_periods)).getText().toString());
        MaxPeriods = Double.parseDouble(((EditText) bigMenuActivity.findViewById(R.id.edit_max_periods)).getText().toString());
        PeriodStep = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_period_step)).getText().toString());
        AmplitudeSteps = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_amplitude_steps)).getText().toString());
        PhaseStep = Double.parseDouble(((EditText) bigMenuActivity.findViewById(R.id.edit_phase_step)).getText().toString());
        HighFreqFilter = Integer.parseInt(((EditText) bigMenuActivity.findViewById(R.id.edit_high_freq_filter)).getText().toString());
        DynamicApproximation = ((CheckBox) bigMenuActivity.findViewById(R.id.dynamic_approximation_checkBox)).isChecked();

        makeParametersList();
        DatabaseManager.updateParametersList(parameterUnitList);
    }

    public void changeValuesInEdit() {
        ((EditText) bigMenuActivity.findViewById(R.id.edit_number_of_modes)).setText(String.valueOf(NumberOfModes));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_number_of_harmonics)).setText(String.valueOf(NumberOfHarmonics));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_period_of_approximation)).setText(String.valueOf(PeriodOfApproximation));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_predicted_bars)).setText(String.valueOf(PredictedBars));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_min_periods)).setText(String.valueOf(MinPeriods));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_max_periods)).setText(String.valueOf(MaxPeriods));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_period_step)).setText(String.valueOf(PeriodStep));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_amplitude_steps)).setText(String.valueOf(AmplitudeSteps));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_phase_step)).setText(String.valueOf(PhaseStep));
        ((EditText) bigMenuActivity.findViewById(R.id.edit_high_freq_filter)).setText(String.valueOf(HighFreqFilter));
        ((CheckBox) bigMenuActivity.findViewById(R.id.dynamic_approximation_checkBox)).setChecked(DynamicApproximation);
    }

    private void loadParameters() {
        for (ParameterUnit parameterUnit: parameterUnitList) {
            switch (parameterUnit.getName()) {
                case "NumberOfModes":
                    NumberOfModes = parameterUnit.getIntValue();
                    break;
                case "NumberOfHarmonics":
                    NumberOfHarmonics = parameterUnit.getIntValue();
                    break;
                case "PeriodOfApproximation":
                    PeriodOfApproximation = parameterUnit.getIntValue();
                    break;
                case "PredictedBars":
                    PredictedBars = parameterUnit.getIntValue();
                    break;
                case "MinPeriods":
                    MinPeriods = parameterUnit.getDoubleValue();
                    break;
                case "MaxPeriods":
                    MaxPeriods = parameterUnit.getDoubleValue();
                    break;
                case "PeriodStep":
                    PeriodStep = parameterUnit.getIntValue();
                    break;
                case "AmplitudeSteps":
                    AmplitudeSteps = parameterUnit.getIntValue();
                    break;
                case "PhaseStep":
                    PhaseStep = parameterUnit.getDoubleValue();
                    break;
                case "HighFreqFilter":
                    HighFreqFilter = parameterUnit.getIntValue();
                    break;
                case "DynamicApproximation":
                    DynamicApproximation = parameterUnit.getIntValue() != 0;
                    break;
            }
        }
    }

    private void makeParametersList() {
        parameterUnitList.clear();

        parameterUnitList.add(new ParameterUnit("NumberOfModes", NumberOfModes, 0));
        parameterUnitList.add(new ParameterUnit("NumberOfHarmonics", NumberOfHarmonics, 0));
        parameterUnitList.add(new ParameterUnit("PeriodOfApproximation", PeriodOfApproximation, 0));
        parameterUnitList.add(new ParameterUnit("PredictedBars", PredictedBars, 0));
        parameterUnitList.add(new ParameterUnit("MinPeriods", 0, MinPeriods));
        parameterUnitList.add(new ParameterUnit("MaxPeriods", 0, MaxPeriods));
        parameterUnitList.add(new ParameterUnit("PeriodStep", PeriodStep, 0));
        parameterUnitList.add(new ParameterUnit("AmplitudeSteps", AmplitudeSteps, 0));
        parameterUnitList.add(new ParameterUnit("PhaseStep", 0, PhaseStep));
        parameterUnitList.add(new ParameterUnit("HighFreqFilter", HighFreqFilter, 0));
        if (DynamicApproximation)
            parameterUnitList.add(new ParameterUnit("DynamicApproximation", 1, 0));
        else
            parameterUnitList.add(new ParameterUnit("DynamicApproximation", 0, 0));
    }

    public List<ParameterUnit> getParameterUnitList() {
        return parameterUnitList;
    }

}




