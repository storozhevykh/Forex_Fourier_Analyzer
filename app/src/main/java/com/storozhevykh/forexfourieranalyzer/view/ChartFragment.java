package com.storozhevykh.forexfourieranalyzer.view;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.controller.ActivityRequestInterface;
import com.storozhevykh.forexfourieranalyzer.controller.RecyclerAdapter;

public class ChartFragment extends Fragment {

    public static final String TAG = "ChartFragment";
    private RecyclerView recycler;
    private RecyclerAdapter recyclerAdapter;


    public static ChartFragment newInstance() {
        return new ChartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.chart_fragment, container, false);

        recycler = fragmentView.findViewById(R.id.recycler_chart);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        recycler.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapter(4, ChartActivity.getChartScale());
        recycler.setAdapter(recyclerAdapter);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public RecyclerAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }

    public void updateRecyclerAdapter(Activity context) {
        Log.d("MyRequest", "Updating recyclerAdapter, scale: " + ChartActivity.getChartScale());
        recyclerAdapter = new RecyclerAdapter(4, ChartActivity.getChartScale());
        recycler.setAdapter(null);
        recycler.setLayoutManager(null);
        recycler.getRecycledViewPool().clear();
        recycler.swapAdapter(recyclerAdapter, true);
        recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        recyclerAdapter.notifyDataSetChanged();
    }
}