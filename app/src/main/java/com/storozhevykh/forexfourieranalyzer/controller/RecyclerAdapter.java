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
        if(position > predictedBars) {
            BarItem barItem = ChartActivity.getBarsList().get(position - predictedBars);
            holder.bind(barItem.getDatetime(), String.valueOf(barItem.getHigh()), barItem.getHigh(), barItem.getLow(), barItem.getOpen() < barItem.getClose(), scale);
        }
        else
            holder.bind2();
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

        void bind(String date, String close, int high, int low, boolean fill, int scale) {
            android.view.ViewGroup.LayoutParams layoutParams = bar.getLayoutParams();
            layoutParams.height = parent.getHeight();
            layoutParams.width = (int) Math.round(Math.pow(2, scale + 2));
            bar.setBackground(new BarDrawable((int) Math.round(Math.pow(2, scale + 1)), high, (int) Math.round(Math.pow(2, scale + 2)), low, fill));
            bar.invalidate();
        }
        void bind2( ) {
            android.view.ViewGroup.LayoutParams layoutParams = bar.getLayoutParams();
            layoutParams.height = parent.getHeight();
            layoutParams.width = (int) Math.round(Math.pow(2, scale + 2));
            bar.setBackgroundColor(Color.BLUE);
            bar.invalidate();
        }
    }
}
