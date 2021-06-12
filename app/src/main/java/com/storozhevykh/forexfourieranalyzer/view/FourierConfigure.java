package com.storozhevykh.forexfourieranalyzer.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storozhevykh.forexfourieranalyzer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourierConfigure#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourierConfigure extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "FourierConfigure";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FourierConfigure() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FourierConfigure.
     */
    // TODO: Rename and change types and number of parameters
    public static FourierConfigure newInstance(String param1, String param2) {
        FourierConfigure fragment = new FourierConfigure();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fourier_configure, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        BigMenuActivity bigMenuActivity = (BigMenuActivity) getActivity();
        bigMenuActivity.getParametersHandler().changeValuesInEdit();

        //Set onClick to help buttons
        getActivity().findViewById(R.id.btn_help_number_of_modes).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_number_of_harmonics).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_period_of_approximation).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_predicted_bars).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_min_periods).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_max_periods).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_period_step).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_amplitude_steps).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_phase_step).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_high_freq_filter).setOnClickListener(bigMenuActivity);
        getActivity().findViewById(R.id.btn_help_dynamic_approximation).setOnClickListener(bigMenuActivity);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((BigMenuActivity) getActivity()).getParametersHandler().setValuesFromEdit();
    }

}