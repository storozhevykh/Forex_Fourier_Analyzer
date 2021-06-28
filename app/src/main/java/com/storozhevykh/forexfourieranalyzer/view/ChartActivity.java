package com.storozhevykh.forexfourieranalyzer.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.controller.ActivityRequestInterface;
import com.storozhevykh.forexfourieranalyzer.controller.RecyclerAdapter;
import com.storozhevykh.forexfourieranalyzer.quotes.BarItem;
import com.storozhevykh.forexfourieranalyzer.quotes.Quote;
import com.storozhevykh.forexfourieranalyzer.retrofit.RetrofitRequest;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements ActivityRequestInterface, View.OnClickListener {

    private static List<Quote> quoteList;
    private static List<BarItem> barsList;
    private ChartFragment chartFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private double maxHigh;
    private double minLow;

    private int barsInHistory;
    private String symbol;
    private String timeframe;

    private TextView scaleMinus;
    private TextView scalePlus;

    public static int chartScale = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        barsInHistory = getIntent().getIntExtra("bars", 100);
        symbol = getResources().getStringArray(R.array.pair_request)[getIntent().getIntExtra("symbol", 0)];
        timeframe = getResources().getStringArray(R.array.tf_request)[getIntent().getIntExtra("timeframe", 0)];

        scaleMinus = findViewById(R.id.scale_minus);
        scalePlus = findViewById(R.id.scale_plus);
        scaleMinus.setOnClickListener(this);
        scalePlus.setOnClickListener(this);

        chartFragment = new ChartFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.chart_fragment_container, chartFragment, ChartFragment.TAG);
        fragmentTransaction.commit();

        quoteList = RetrofitRequest.getInstance().getQuotes(this, symbol, timeframe, barsInHistory);
    }

    private void updateFragment() {
        chartFragment = new ChartFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chart_fragment_container, chartFragment, ChartFragment.TAG);
        fragmentTransaction.commit();

                new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                chartFragment.getRecyclerAdapter().notifyDataSetChanged();
            }
        }.execute();
    }

    public void showQuotes() {
        double onePixelPrice = 0;

        if (quoteList != null) {

            onePixelPrice = calculateOnePixelPrice();
            barsList = convertQuotesToBars(onePixelPrice);

            RecyclerAdapter recyclerAdapter = chartFragment.getRecyclerAdapter();
            recyclerAdapter.notifyDataSetChanged();
            findViewById(R.id.quotes_loading_indicator).setVisibility(View.INVISIBLE);
            findViewById(R.id.quotes_loading_text).setVisibility(View.INVISIBLE);
        }
        else
            Log.d("MyRequest", "Smth went wrong, quoteList is null");
    }

    @Override
    public Fragment getFragment() {
        return chartFragment;
    }

    private double calculateOnePixelPrice() {
        maxHigh = 0;
        minLow = 999999;
        double chartRange = 0;
        int fragmentHeight = 0;
        double onePixelPrice = 0;

        for (Quote q : quoteList) {
            if (q.getHigh() > maxHigh)
                maxHigh = q.getHigh();
            if (q.getLow() < minLow)
                minLow = q.getLow();
        }
        chartRange = (maxHigh - minLow) * 1.0;
        fragmentHeight = findViewById(R.id.chart_fragment_container).getHeight();
        if (fragmentHeight > 0)
        onePixelPrice = chartRange / fragmentHeight;
        Log.d("MyRequest", "fragmentHeight = " + fragmentHeight + "; onePixelPrice = " + onePixelPrice + "; chartRange = " + chartRange);
        return onePixelPrice;
    }

    private List<BarItem> convertQuotesToBars(double onePixelPrice) {

        int barHigh;
        int barLow;
        int barOpen;
        int barClose;
        List<BarItem> bars = new ArrayList<>();

        for (Quote q : quoteList) {
            barHigh = (int) Math.round((maxHigh - q.getHigh()) / onePixelPrice);
            barOpen = (int) Math.round((maxHigh - q.getOpen()) / onePixelPrice);
            barClose = (int) Math.round((maxHigh - q.getClose()) / onePixelPrice);
            barLow =  barHigh + (int) Math.round((q.getHigh() - q.getLow()) / onePixelPrice);

            bars.add(new BarItem(q.getDatetime(), barOpen, barHigh, barLow, barClose));
        }

        return bars;
    }

    public static List<Quote> getQuoteList() {
        return quoteList;
    }

    public static List<BarItem> getBarsList() {
        return barsList;
    }

    @Override
    public void onClick(View v) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        switch (v.getId()) {
            case R.id.scale_minus:
                if (chartScale > 0) {
                    chartScale--;
                    scaleMinus.startAnimation(animation);
                    updateFragment();
                }
                break;

            case R.id.scale_plus:
                if (chartScale < 8) {
                    chartScale++;
                    scalePlus.startAnimation(animation);
                    updateFragment();
                }
                break;
        }
        if (chartScale <= 0)
            scaleMinus.setTextColor(getResources().getColor(R.color.disabled));
        else
            scaleMinus.setTextColor(getResources().getColor(R.color.black));
        if (chartScale >= 8)
            scalePlus.setTextColor(getResources().getColor(R.color.disabled));
        else
            scalePlus.setTextColor(getResources().getColor(R.color.black));

    }

    public static int getChartScale() {
        return chartScale;
    }

}