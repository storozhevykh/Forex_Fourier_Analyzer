package com.storozhevykh.forexfourieranalyzer.view;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.storozhevykh.forexfourieranalyzer.controller.ParametersHandler;
import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.controller.ParametersOwner;

public class BigMenuActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, TextWatcher {

    private TextView btn_goToChart;
    private Spinner spinner;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment ChartConfigureFragment;
    private Fragment FourierConfigureFragment;
    private ParametersHandler parametersHandler = new ParametersHandler(this);

    private DialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_menu);

        btn_goToChart = findViewById(R.id.btn_go_to_chart);
        btn_goToChart.setOnClickListener(this);

        //Configure spinner
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,getResources().getStringArray(R.array.big_menu));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //spinner.setBackgroundResource(R.drawable.get_started_border);
        //spinner.getBackground().setColorFilter(getResources().getColor(R.color.top_bottom_fields), PorterDuff.Mode.SRC_ATOP);

        //Configure fragments
        ChartConfigureFragment = new ChartConfigure();
        FourierConfigureFragment = new FourierConfigure();
        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, FourierConfigureFragment, FourierConfigure.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0 && fragmentManager.findFragmentByTag(ChartConfigure.TAG) != null) {
            if (parametersHandler.checkChartParameters(1)) {
                if (fragmentManager.findFragmentByTag(FourierConfigure.TAG) == null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, FourierConfigureFragment, FourierConfigure.TAG);
                    fragmentTransaction.commit();
                    parametersHandler.saveParametersToDatabase();
                }
            }
            else {
                spinner.setSelection(1);
                return;
            }
        }
        if (position == 1 && fragmentManager.findFragmentByTag(FourierConfigure.TAG) != null) {
            if (parametersHandler.checkFourierParameters(1)) {
                if (fragmentManager.findFragmentByTag(ChartConfigure.TAG) == null) {
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, ChartConfigureFragment, ChartConfigure.TAG);
                    fragmentTransaction.commit();
                    parametersHandler.saveParametersToDatabase();
                }
            }
            else {
                spinner.setSelection(0);
                return;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public ParametersHandler getParametersHandler() {
        return parametersHandler;
    }

    @Override
    public void onClick(View v) {
        dialog = null;
        switch (v.getId()) {
            //Help cases
            case R.id.btn_help_number_of_modes:
                dialog = new HelpDialogFragment(getResources().getString(R.string.number_of_modes_help_text), getResources().getString(R.string.parameter_number_of_modes));
                break;
            case R.id.btn_help_number_of_harmonics:
                dialog = new HelpDialogFragment(getResources().getString(R.string.number_of_harmonics_help_text), getResources().getString(R.string.parameter_number_of_harmonics));
                break;
            case R.id.btn_help_period_of_approximation:
                dialog = new HelpDialogFragment(getResources().getString(R.string.period_of_approximation_help_text), getResources().getString(R.string.parameter_period_of_approximation));
                break;
            case R.id.btn_help_predicted_bars:
                dialog = new HelpDialogFragment(getResources().getString(R.string.predicted_bars_help_text), getResources().getString(R.string.parameter_predicted_bars));
                break;
            case R.id.btn_help_min_periods:
                dialog = new HelpDialogFragment(getResources().getString(R.string.min_periods_help_text), getResources().getString(R.string.parameter_min_periods));
                break;
            case R.id.btn_help_max_periods:
                dialog = new HelpDialogFragment(getResources().getString(R.string.max_periods_help_text), getResources().getString(R.string.parameter_max_periods));
                break;
            case R.id.btn_help_period_step:
                dialog = new HelpDialogFragment(getResources().getString(R.string.period_step_help_text), getResources().getString(R.string.parameter_period_step));
                break;
            case R.id.btn_help_amplitude_steps:
                dialog = new HelpDialogFragment(getResources().getString(R.string.amplitude_steps_help_text), getResources().getString(R.string.parameter_amplitude_steps));
                break;
            case R.id.btn_help_phase_step:
                dialog = new HelpDialogFragment(getResources().getString(R.string.phase_step_help_text), getResources().getString(R.string.parameter_phase_step));
                break;
            case R.id.btn_help_high_freq_filter:
                dialog = new HelpDialogFragment(getResources().getString(R.string.high_freq_filter_help_text), getResources().getString(R.string.parameter_high_freq_filter));
                break;
            case R.id.btn_help_dynamic_approximation:
                dialog = new HelpDialogFragment(getResources().getString(R.string.dynamic_approximation_help_text), getResources().getString(R.string.parameter_dynamic_approximation));
                break;
            case R.id.btn_help_bars_in_history:
                dialog = new HelpDialogFragment(getResources().getString(R.string.bars_in_history_help_text), getResources().getString(R.string.parameter_bars_in_history));
                break;
                //Error cases
            case R.id.btn_error_number_of_modes:
                dialog = new HelpDialogFragment(getResources().getString(R.string.number_of_modes_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_number_of_harmonics:
                dialog = new HelpDialogFragment(getResources().getString(R.string.number_of_harmonics_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_period_of_approximation:
                dialog = new HelpDialogFragment(getResources().getString(R.string.period_of_approximation_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_predicted_bars:
                dialog = new HelpDialogFragment(getResources().getString(R.string.predicted_bars_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_min_periods:
                dialog = new HelpDialogFragment(getResources().getString(R.string.min_periods_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_max_periods:
                dialog = new HelpDialogFragment(getResources().getString(R.string.max_periods_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_period_step:
                dialog = new HelpDialogFragment(getResources().getString(R.string.period_step_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_amplitude_steps:
                dialog = new HelpDialogFragment(getResources().getString(R.string.amplitude_steps_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_phase_step:
                dialog = new HelpDialogFragment(getResources().getString(R.string.phase_step_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_high_freq_filter:
                dialog = new HelpDialogFragment(getResources().getString(R.string.high_freq_filter_error_text), getResources().getString(R.string.error_message));
                break;
            case R.id.btn_error_bars_in_history:
                dialog = new HelpDialogFragment(getResources().getString(R.string.bars_in_history_error_text), getResources().getString(R.string.error_message));
                break;
        }
        if (dialog != null)
        dialog.show(fragmentManager, "help");

        //Set default parameters button
        if (v.getId() == R.id.btn_set_default) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.set_default_btn_anim);
            Animation animationReverse = AnimationUtils.loadAnimation(this, R.anim.set_default_btn_anim_reverse);
            TextView textView = findViewById(R.id.btn_set_default);
            TextView textViewBack = findViewById(R.id.btn_set_default_back);
            /*textView.setTextColor(getResources().getColor(R.color.get_started_clicked));
            textView.setBackgroundColor(getResources().getColor(R.color.get_started));*/
            textView.startAnimation(animation);
            textViewBack.startAnimation(animationReverse);

            parametersHandler.setDefaultValues();
            parametersHandler.changeFourierValuesInEdit();
        }

        //Go to chart button
        if (v.getId() == R.id.btn_go_to_chart) {
            if (parametersHandler.checkChartParameters(1) && parametersHandler.checkFourierParameters(1)) {
                parametersHandler.saveParametersToDatabase();
                ParametersOwner.getInstance().setParameters(parametersHandler.getParameterUnitList());
                Intent intent = new Intent(this, ChartActivity.class);
                intent.putExtra("bars", parametersHandler.getMaxBarsInHistory());
                intent.putExtra("symbol", parametersHandler.getSelectedPairIndex());
                intent.putExtra("timeframe", parametersHandler.getSelectedTFIndex());
                startActivity(intent);
            }

        }
    }

    //Methods of TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        if (fragmentManager.findFragmentByTag(FourierConfigure.TAG) != null) {
            if (s == ((EditText) findViewById(R.id.edit_number_of_modes)).getText()) {
                parametersHandler.setNumberOfModes(parametersHandler.safeParseInt(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_number_of_harmonics)).getText()) {
                parametersHandler.setNumberOfHarmonics(parametersHandler.safeParseInt(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_period_of_approximation)).getText()) {
                parametersHandler.setPeriodOfApproximation(parametersHandler.safeParseInt(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_predicted_bars)).getText()) {
                parametersHandler.setPredictedBars(parametersHandler.safeParseInt(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_period_step)).getText()) {
                parametersHandler.setPeriodStep(parametersHandler.safeParseInt(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_min_periods)).getText()) {
                parametersHandler.setMinPeriods(parametersHandler.safeParseDouble(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_max_periods)).getText()) {
                parametersHandler.setMaxPeriods(parametersHandler.safeParseDouble(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_amplitude_steps)).getText()) {
                parametersHandler.setAmplitudeSteps(parametersHandler.safeParseInt(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_phase_step)).getText()) {
                parametersHandler.setPhaseStep(parametersHandler.safeParseDouble(s.toString()));
            }
            if (s == ((EditText) findViewById(R.id.edit_high_freq_filter)).getText()) {
                parametersHandler.setHighFreqFilter(parametersHandler.safeParseInt(s.toString()));
            }
            parametersHandler.checkFourierParameters(0);
        }

        if (fragmentManager.findFragmentByTag(ChartConfigure.TAG) != null) {
            if (s == ((EditText) findViewById(R.id.edit_bars_in_history)).getText()) {
                parametersHandler.setMaxBarsInHistory(parametersHandler.safeParseInt(s.toString()));
            }
            parametersHandler.checkChartParameters(0);
        }
    }
}