package com.storozhevykh.forexfourieranalyzer.retrofit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.storozhevykh.forexfourieranalyzer.controller.ActivityRequestInterface;
import com.storozhevykh.forexfourieranalyzer.controller.ParametersHandler;
import com.storozhevykh.forexfourieranalyzer.quotes.FullResponse;
import com.storozhevykh.forexfourieranalyzer.quotes.Quote;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Classical Singleton
public class RetrofitRequest {

    private static RetrofitInterface api;
    private static Retrofit retrofit;
    private static RetrofitRequest instance;

    private static final String API_KEY = "16ba72cb1e5e4940bba44440d5c3438e";

    private RetrofitRequest() {
    }

    public static RetrofitRequest getInstance() {
        if (instance == null) {
            instance = new RetrofitRequest();
            retrofit = instance.createRetrofit();
            api = retrofit.create(RetrofitInterface.class);
        }
        return instance;
    }

    public List<Quote> getQuotes(ActivityRequestInterface context, String symbol, String timeframe, int bars) {

        Log.d("MyRequest", "Starting GetQuotes");
        List<Quote> quoteList = new ArrayList<>();
        api.getQuotes(symbol, timeframe, bars, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<FullResponse>() {
                    @Override
                    public void onSuccess(@NonNull FullResponse fullResponse) {
                        List<Quote> quoteResp = fullResponse.getValues();
                        Log.d("MyRequest", "Response size: " + quoteResp.size());
                        if (quoteResp != null) {
                            for (Quote q : quoteResp) {
                                quoteList.add(q);
                            }

                        }
                        else
                            Log.d("MyRequest", "Response is null");
                        Log.d("MyRequest", "This was RxJava");
                        context.showQuotes();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("MyRequest", "Error: " + e.getMessage());
                    }
                });
        return quoteList;
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.twelvedata.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
}
