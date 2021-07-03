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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.controller.ActivityRequestInterface;
import com.storozhevykh.forexfourieranalyzer.controller.ParametersOwner;
import com.storozhevykh.forexfourieranalyzer.controller.RecyclerAdapter;
import com.storozhevykh.forexfourieranalyzer.model.FourierCalculationLogic;
import com.storozhevykh.forexfourieranalyzer.quotes.BarItem;
import com.storozhevykh.forexfourieranalyzer.quotes.Quote;
import com.storozhevykh.forexfourieranalyzer.retrofit.RetrofitRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements ActivityRequestInterface, View.OnClickListener {

    private static List<Quote> quoteList;
    private static List<BarItem> barsList;
    private static List<Double> calcApproximationList;
    private static List<Double> calcPredictionList;
    private static List<Integer> predictionList;
    private ChartFragment chartFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Handler handler;

    private double maxHigh;
    private double minLow;
    private double onePixelPrice;

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

        ProgressBar progressBar = findViewById(R.id.quotes_loading_indicator);
        TextView percent = findViewById(R.id.progress_percent);
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                progressBar.setProgress(msg.what);
                percent.setText((int) Math.round(100 * msg.what / progressBar.getMax()) + "%");
                //Log.d("calc", "handleMessage: " + msg.what);
            }
        };
        calcPredictionList = new ArrayList<>();
        predictionList = new ArrayList<>();
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
        onePixelPrice = 0;

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

            bars.add(new BarItem(q.getDatetime(), barOpen, barHigh, barLow, barClose, 0));
        }

        return bars;
    }

    private void updateBarList() {
        int fourier;
        //Log.d("calc", "calcResultList.size() = " + calcResultList.size());
        for (int i = 0; i < calcApproximationList.size(); i++) {
            fourier = (int) Math.round((maxHigh - calcApproximationList.get(i)) / onePixelPrice);
            barsList.get(i).setFourier(fourier);
        }

        for (int i = 0; i < calcPredictionList.size(); i++) {
            fourier = (int) Math.round((maxHigh - calcPredictionList.get(i)) / onePixelPrice);
            predictionList.add(fourier);
        }

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

    public static List<Integer> getPredictionList() {
        return predictionList;
    }

    public void calculate(View view) {

        FourierCalculationLogic calculationObject = new FourierCalculationLogic(quoteList, handler);
        int iterations = calculationObject.getIterations();

        ProgressBar progressBar = findViewById(R.id.quotes_loading_indicator);
        progressBar.setMax(iterations);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        TextView percent = findViewById(R.id.progress_percent);
        percent.setVisibility(View.VISIBLE);
        Log.d("calc", "progressBar.getMax() = " + progressBar.getMax());
        Log.d("calc", "progressBar.getProgress() = " + progressBar.getProgress());

        new AsyncTask<Void, Integer, List<Double[]>>() {

            @Override
            protected List<Double[]> doInBackground(Void... voids) {
                Log.d("calc", "Calculation started");
                //FourierCalculationLogic calculationObject = new FourierCalculationLogic(lists[0], handler);
                return calculationObject.calculate(maxHigh, minLow);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(List<Double[]> doubles) {
                Log.d("calc", "Calculation finished");
                calcApproximationList = Arrays.asList(doubles.get(0).clone());
                calcPredictionList = Arrays.asList(doubles.get(1).clone());
                updateBarList();
                progressBar.setVisibility(View.INVISIBLE);
                percent.setVisibility(View.INVISIBLE);
                chartFragment.getRecyclerAdapter().notifyDataSetChanged();
            }
        }.execute();
        /*FourierCalculationLogic calculationObject = new FourierCalculationLogic(quoteList);
        calcResultList = Arrays.asList(calculationObject.calculate(maxHigh, minLow).clone());
        updateBarList();
        chartFragment.getRecyclerAdapter().notifyDataSetChanged();*/
    }
}