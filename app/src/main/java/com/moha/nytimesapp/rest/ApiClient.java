package com.moha.nytimesapp.rest;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final int TIMEOUT = 30000;
    private static final String BASE_URL = "https://api.nytimes.com/";
    private static Retrofit retrofit = null;

    private static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT,TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT,TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(new MyInterceptor())
            .build();

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

   private static class MyInterceptor implements Interceptor{

        @NonNull
        @SuppressLint("DefaultLocale")
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            System.out.println(
                    String.format("Sending request %s on %n%s , %s", request.url(), request.headers(), request.body()));

            Response response = chain.proceed(request);
            long t2= System.nanoTime();

            System.out.println(
                    String.format("Received response for %s in %.1fms%n%s", response.request().url(),
                            (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
