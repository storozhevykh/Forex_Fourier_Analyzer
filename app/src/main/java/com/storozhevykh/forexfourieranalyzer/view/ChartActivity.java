package com.storozhevykh.forexfourieranalyzer.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.storozhevykh.forexfourieranalyzer.R;
import com.storozhevykh.forexfourieranalyzer.controller.ActivityRequestInterface;
import com.storozhevykh.forexfourieranalyzer.controller.MoreSpinnerAdapter;
import com.storozhevykh.forexfourieranalyzer.controller.MoreSpinnerObject;
import com.storozhevykh.forexfourieranalyzer.controller.ParametersOwner;
import com.storozhevykh.forexfourieranalyzer.controller.RecyclerAdapter;
import com.storozhevykh.forexfourieranalyzer.model.ChartRightDrawable;
import com.storozhevykh.forexfourieranalyzer.model.FourierCalculationLogic;
import com.storozhevykh.forexfourieranalyzer.model.StaticStorage;
import com.storozhevykh.forexfourieranalyzer.quotes.BarItem;
import com.storozhevykh.forexfourieranalyzer.quotes.Quote;
import com.storozhevykh.forexfourieranalyzer.retrofit.RetrofitRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements ActivityRequestInterface, View.OnClickListener {

    private static List<Quote> quoteList;
    private static List<BarItem> barsList;
    private static List<Double> calcApproximationList;
    private static List<Double> calcPredictionList;
    private static List<Double> calcBaseLineList;
    private static List<List<Double>> calcHarmonicsApproximationList = new ArrayList<>();
    private static List<List<Double>> calcHarmonicsPredictionList = new ArrayList<>();
    private static List<Integer> predictionList;
    private static List<List<Integer>> harmonicsPredictionList = new ArrayList<>();
    private ChartFragment chartFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Handler handler;
    private Spinner moreSpinner;

    private double maxHigh = 0;
    private double minLow = 999999;
    private double onePixelPrice;

    private int barsInHistory;
    private String symbol;
    private String timeframe;

    private TextView scaleMinus;
    private TextView scalePlus;

    public static int chartScale = 2;

    private List<MoreSpinnerObject> spinnerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Log.d("mylog", "onCreate");
        System.out.println("onCreate");

        barsInHistory = getIntent().getIntExtra("bars", 100);
        symbol = getResources().getStringArray(R.array.pair_request)[getIntent().getIntExtra("symbol", 0)];
        timeframe = getResources().getStringArray(R.array.tf_request)[getIntent().getIntExtra("timeframe", 0)];

        scaleMinus = findViewById(R.id.scale_minus);
        scalePlus = findViewById(R.id.scale_plus);
        scaleMinus.setOnClickListener(this);
        scalePlus.setOnClickListener(this);

        moreSpinner = findViewById(R.id.moreSpinner);
        List<String> titles = Arrays.asList(getResources().getStringArray(R.array.more_spinner_titles));
        titles.forEach(title -> {
            spinnerItems.add(new MoreSpinnerObject(title, false));
        });

        MoreSpinnerAdapter adapter = new MoreSpinnerAdapter(this, 0,
                spinnerItems);
        moreSpinner.setAdapter(adapter);

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
        calcApproximationList = StaticStorage.calcApproximationList;
        calcBaseLineList = StaticStorage.calcBaseLineList;
        calcPredictionList = StaticStorage.calcPredictionList;
        predictionList = StaticStorage.predictionList;
        updateFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    private void updateFragment() {
        Log.d("mylog", "updateFragment");
        System.out.println("updateFragment");
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
        Log.d("mylog", "showQuotes");
        System.out.println("showQuotes");

        if (quoteList != null) {

            onePixelPrice = calculateOnePixelPrice();
            barsList = convertQuotesToBars(onePixelPrice);
            System.out.println("barsList size = " + barsList.size());

            RecyclerAdapter recyclerAdapter = chartFragment.getRecyclerAdapter();
            recyclerAdapter.notifyDataSetChanged();
            findViewById(R.id.quotes_loading_indicator).setVisibility(View.INVISIBLE);
            findViewById(R.id.quotes_loading_text).setVisibility(View.INVISIBLE);
            drawChartRight();
            updateBarList();
        }
        else
            Log.d("MyRequest", "Smth went wrong, quoteList is null");
    }

    @Override
    public Fragment getFragment() {
        return chartFragment;
    }

    private double calculateOnePixelPrice() {
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

            bars.add(new BarItem(q.getDatetime(), barOpen, barHigh, barLow, barClose, 0, 0, null));
        }

        return bars;
    }

    private void drawChartRight () {
        RelativeLayout chartRight = findViewById(R.id.chart_right);
        int dashIncrement = (int) (Math.round((maxHigh - minLow) / onePixelPrice)) / 8;
        double priceIncrement = (maxHigh - minLow) / 8;
        Drawable chartRightBack = new ChartRightDrawable(chartRight.getHeight(), dashIncrement, 7, maxHigh, priceIncrement);
        chartRight.setBackground(chartRightBack);
    }

    public void updateBarList() {
        int fourier;
        int baseLine;

        Log.d("mylog", "updateBarList");
        System.out.println("updateBarList");

        /*if (calcPredictionList != null && !calcPredictionList.isEmpty()) {
            if (Collections.max(calcPredictionList) > maxHigh || Collections.min(calcPredictionList) < minLow) {
                if (Collections.max(calcPredictionList) > maxHigh)
                    maxHigh = Collections.max(calcPredictionList);
                if (Collections.min(calcPredictionList) < minLow)
                    minLow = Collections.min(calcPredictionList);
                showQuotes();
            }
        }*/

        if (calcApproximationList != null && calcPredictionList != null && !barsList.isEmpty()) {

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {

                    for (int i = 0; i < calcApproximationList.size(); i++) {

                        if (spinnerItems.get(1).isSelected() && barsList.get(i).getFourier() == 0) {
                            int fourier = (int) Math.round((maxHigh - calcApproximationList.get(i)) / onePixelPrice);
                            barsList.get(i).setFourier(fourier);
                        } else if (!spinnerItems.get(1).isSelected() && barsList.get(i).getFourier() != 0)
                            barsList.get(i).setFourier(0);

                        if (spinnerItems.get(2).isSelected() && barsList.get(i).getBaseLine() == 0) {
                            int baseLine = (int) Math.round((maxHigh - calcBaseLineList.get(i)) / onePixelPrice);
                            barsList.get(i).setBaseLine(baseLine);
                        } else if (!spinnerItems.get(2).isSelected() && barsList.get(i).getBaseLine() != 0)
                            barsList.get(i).setBaseLine(0);
                    }

                    if (spinnerItems.get(1).isSelected() && predictionList.isEmpty()) {
                        for (int i = 0; i < calcPredictionList.size(); i++) {
                            int fourier = (int) Math.round((maxHigh - calcPredictionList.get(i)) / onePixelPrice);
                            predictionList.add(fourier);
                        }
                        StaticStorage.predictionList = predictionList;
                    }
                    else if (!spinnerItems.get(1).isSelected() && !predictionList.isEmpty()) {
                        predictionList.clear();
                        StaticStorage.predictionList = predictionList;
                    }

                    if (calcHarmonicsApproximationList != null && calcHarmonicsPredictionList != null) {
                        if (spinnerItems.get(3).isSelected() && barsList.get(0).getHarmonics() == null) {

                            harmonicsPredictionList.clear();

                            for (int harmonic = 0; harmonic < calcHarmonicsApproximationList.size(); harmonic++) {
                                List<Double> curHarmonicArr = calcHarmonicsApproximationList.get(harmonic);

                                for (int i = 0; i < curHarmonicArr.size(); i++) {
                                    int fourier = (int) Math.round((maxHigh - curHarmonicArr.get(i)) / onePixelPrice);
                                    if (barsList.get(i).getHarmonics() == null)
                                        barsList.get(i).setHarmonics(new ArrayList<>());
                                    barsList.get(i).getHarmonics().add(fourier);
                                }

                                curHarmonicArr = calcHarmonicsPredictionList.get(harmonic);
                                harmonicsPredictionList.add(new ArrayList<>());

                                for (int i = 0; i < curHarmonicArr.size(); i++) {
                                    int fourier = (int) Math.round((maxHigh - curHarmonicArr.get(i)) / onePixelPrice);
                                    harmonicsPredictionList.get(harmonic).add(fourier);
                                }
                            }
                        }
                    }

                    if (!spinnerItems.get(3).isSelected() && barsList.get(0).getHarmonics() != null && calcApproximationList != null) {
                        for (int i = 0; i < calcApproximationList.size(); i++) {
                            barsList.get(i).setHarmonics(null);
                        }
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void unused) {
                    chartFragment.getRecyclerAdapter().notifyDataSetChanged();
                    System.out.println(chartFragment.getRecyclerAdapter());
                }
            }.execute();
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
                if (chartScale < 5) {
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
        if (chartScale >= 5)
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

        Log.d("mylog", "calculate");
        System.out.println("calculate");
        /*Log.d("MyLog", "spinnerItems size: " + spinnerItems.size());
        spinnerItems.forEach(item -> Log.d("MyLog", item.getTitle() + ": " + item.isSelected()));*/
        /*System.out.println("spinnerItems size: " + spinnerItems.size());
        spinnerItems.forEach(item -> System.out.println(item.getTitle() + ": " + item.isSelected()));*/

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

        new AsyncTask<Void, Integer, List<List<Double[]>>>() {

            @Override
            protected List<List<Double[]>> doInBackground(Void... voids) {
                Log.d("calc", "Calculation started");
                //FourierCalculationLogic calculationObject = new FourierCalculationLogic(lists[0], handler);
                return calculationObject.calculate(maxHigh, minLow);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(List<List<Double[]>> doubles) {
                Log.d("calc", "Calculation finished");
                calcApproximationList = Arrays.asList(doubles.get(0).get(0).clone());
                calcPredictionList = Arrays.asList(doubles.get(0).get(1).clone());
                calcBaseLineList = Arrays.asList(doubles.get(0).get(2).clone());
                for (int i = 1; i < doubles.size(); i++) {
                    calcHarmonicsApproximationList.add(Arrays.asList(doubles.get(i).get(0).clone()));
                    calcHarmonicsPredictionList.add(Arrays.asList(doubles.get(i).get(1).clone()));
                }
                updateBarList();
                progressBar.setVisibility(View.INVISIBLE);
                percent.setVisibility(View.INVISIBLE);

                StaticStorage.calcApproximationList = calcApproximationList;
                StaticStorage.calcBaseLineList = calcBaseLineList;
                StaticStorage.calcPredictionList = calcPredictionList;
            }
        }.execute();
        /*FourierCalculationLogic calculationObject = new FourierCalculationLogic(quoteList);
        calcResultList = Arrays.asList(calculationObject.calculate(maxHigh, minLow).clone());
        updateBarList();
        chartFragment.getRecyclerAdapter().notifyDataSetChanged();*/
    }
}