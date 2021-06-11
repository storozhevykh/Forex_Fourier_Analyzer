package com.storozhevykh.forexfourieranalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BigMenuActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment ChartConfigureFragment;
    private Fragment FourierConfigureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_menu);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item,getResources().getStringArray(R.array.big_menu));
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        ChartConfigureFragment = new ChartConfigure();
        FourierConfigureFragment = new FourierConfigure();
        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, ChartConfigureFragment);
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
}