package com.storozhevykh.forexfourieranalyzer.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.model.BarDrawable;
import com.storozhevykh.forexfourieranalyzer.quotes.BarItem;
import com.storozhevykh.forexfourieranalyzer.quotes.Quote;
import com.storozhevykh.forexfourieranalyzer.view.ChartActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private int barsPerHolder;
    private ViewGroup parent;
    private int scale;
    private int count = 0;
    private int predictedBars = 0;

    public RecyclerAdapter(int barsPerHolder, int scale) {
        this.barsPerHolder = barsPerHolder;
        this.scale = scale;
        predictedBars = ParametersOwner.getInstance().getPredictedBars();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        this.parent = parent;
        Context context = parent.getContext();
        int layoutId = R.layout.chart_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        List<Integer> emptyList = new ArrayList<>();
        if(position >= predictedBars) {
            BarItem nextBarItem;
            BarItem barItem = ChartActivity.getBarsList().get(position - predictedBars);
            if (position - predictedBars + 1 >= 0 && position - predictedBars + 1 < ChartActivity.getBarsList().size())
                nextBarItem = ChartActivity.getBarsList().get(position - predictedBars + 1);
            else
                nextBarItem = barItem;
            holder.bind(barItem.getDatetime(), String.valueOf(barItem.getHigh()), barItem.getHigh(), barItem.getLow(), barItem.getOpen() < barItem.getClose(), scale,
                    barItem.getFourier(), nextBarItem.getFourier(), barItem.getBaseLine(), nextBarItem.getBaseLine(), barItem.getHarmonics(), nextBarItem.getHarmonics());
        }
        else if (ChartActivity.getPredictionList().size() > 0) {
            if (position + 1 < ChartActivity.getPredictionList().size())
                holder.bind(scale, ChartActivity.getPredictionList().get(position), ChartActivity.getPredictionList().get(position + 1), emptyList, emptyList);
            else
                holder.bind(scale, ChartActivity.getPredictionList().get(position), ChartActivity.getPredictionList().get(position), emptyList, emptyList);
        }
        else
            holder.bind(scale);
    }

    @Override
    public int getItemCount() {
        return ChartActivity.getQuoteList().size() + predictedBars;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bar;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            bar = itemView.findViewById(R.id.bar_item);
            itemView.invalidate();
        }

        void bind(String date, String close, int high, int low, boolean fill, int scale, int fourier, int nextFourier,
                  int baseLine, int nextBaseLine, List<Integer> harmonics, List<Integer> nextHarmonics) {
            android.view.ViewGroup.LayoutParams layoutParams = bar.getLayoutParams();
            layoutParams.height = parent.getHeight();
            layoutParams.width = (int) Math.round(Math.pow(2, scale + 2));
            bar.setBackground(new BarDrawable((int) Math.round(Math.pow(2, scale + 1)), high, (int) Math.round(Math.pow(2, scale + 2)), low, fill,
                    fourier, nextFourier, baseLine, nextBaseLine, layoutParams.width, harmonics, nextHarmonics));
            bar.invalidate();
        }
        void bind(int scale, int fourier, int nextFourier, List<Integer> harmonics, List<Integer> nextHarmonics) {
            android.view.ViewGroup.LayoutParams layoutParams = bar.getLayoutParams();
            layoutParams.height = parent.getHeight();
            layoutParams.width = (int) Math.round(Math.pow(2, scale + 2));
            bar.setBackground(new BarDrawable((int) Math.round(Math.pow(2, scale + 1)), 0, (int) Math.round(Math.pow(2, scale + 2)), 0, false,
                    fourier, nextFourier ,0, 0, layoutParams.width, harmonics, nextHarmonics));
            bar.invalidate();
        }
        void bind(int scale) {
            android.view.ViewGroup.LayoutParams layoutParams = bar.getLayoutParams();
            layoutParams.height = parent.getHeight();
            layoutParams.width = (int) Math.round(Math.pow(2, scale + 2));
            bar.setBackgroundColor(Color.WHITE);
            bar.invalidate();
        }
    }
}
