package com.storozhevykh.forexfourieranalyzer.retrofit;

import android.app.Activity;
import android.content.Context;

import com.storozhevykh.forexfourieranalyzer.controller.ActivityRequestInterface;
import com.storozhevykh.forexfourieranalyzer.quotes.FullResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    //@GET("/time_series?symbol=USD/JPY&interval=15min&outputsize=100&apikey=16ba72cb1e5e4940bba44440d5c3438e")
    @GET("/time_series")
    Single<FullResponse> getQuotes(@Query("symbol") String symbol, @Query("interval") String timeframe, @Query("outputsize") int bars, @Query("apikey") String apikey);
}
