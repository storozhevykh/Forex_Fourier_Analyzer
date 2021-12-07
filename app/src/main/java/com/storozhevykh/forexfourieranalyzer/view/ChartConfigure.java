package com.storozhevykh.forexfourieranalyzer.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.storozhevykh.forexfourieranalyzer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChartConfigure#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartConfigure extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ChartConfigure";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner pairsSpinner;
    private Spinner tfSpinner;
    BigMenuActivity bigMenuActivity;

    public ChartConfigure() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartConfigure.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartConfigure newInstance(String param1, String param2) {
        ChartConfigure fragment = new ChartConfigure();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bigMenuActivity = (BigMenuActivity) getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart_configure, container, false);
        // Inflate the layout for this fragment
        pairsSpinner = view.findViewById(R.id.choose_pair_spinner);
        ArrayAdapter<String> pairsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.parameters_spinner_item,getResources().getStringArray(R.array.pair_items));
        pairsSpinner.setAdapter(pairsAdapter);
        pairsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bigMenuActivity.getParametersHandler().setSelectedPairIndex(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        tfSpinner = view.findViewById(R.id.choose_tf_spinner);
        ArrayAdapter<String> tfAdapter = new ArrayAdapter<String>(getActivity(), R.layout.parameters_spinner_item,getResources().getStringArray(R.array.tf_items));
        tfSpinner.setAdapter(tfAdapter);
        tfSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                bigMenuActivity.getParametersHandler().setSelectedTFIndex(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        return view;
    }

    public void onStart() {
        super.onStart();
        bigMenuActivity.getParametersHandler().changeChartValuesInEdit();

        getActivity().findViewById(R.id.btn_help_bars_in_history).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_error_bars_in_history).setOnClickListener(bigMenuActivity);
        //Set onTexChangedListener to edit fields
        ((EditText) getActivity().findViewById(R.id.edit_bars_in_history)).addTextChangedListener(bigMenuActivity);
    }

    @Override
    public void onStop() {
        super.onStop();
        //((BigMenuActivity) getActivity()).getParametersHandler().setChartValuesFromEdit();
    }
}