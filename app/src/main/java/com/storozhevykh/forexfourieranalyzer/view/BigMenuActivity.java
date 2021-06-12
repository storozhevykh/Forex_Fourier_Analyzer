package com.storozhevykh.forexfourieranalyzer.view;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.storozhevykh.forexfourieranalyzer.controller.ParametersHandler;
import com.storozhevykh.forexfourieranalyzer.R;

public class BigMenuActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

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

        //Configure spinner
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,getResources().getStringArray(R.array.big_menu));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Configure fragments
        ChartConfigureFragment = new ChartConfigure();
        FourierConfigureFragment = new FourierConfigure();
        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, FourierConfigureFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            if (fragmentManager.findFragmentByTag("FourierConfigure") == null) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, FourierConfigureFragment);
                fragmentTransaction.commit();
            }
        }
        if (position == 1) {
            if (fragmentManager.findFragmentByTag("ChartConfigure") == null) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, ChartConfigureFragment);
                fragmentTransaction.commit();
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
        switch (v.getId()) {
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
        }
        dialog.show(fragmentManager, "help");
    }
}