package com.moha.nytimesapp.dependency.module;

import android.app.Application;

import com.moha.nytimesapp.rest.MyInterceptor;
import com.moha.nytimesapp.rest.WebServiceAPI;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ServiceModule {
    private static final String BASE_URL = "https://api.nytimes.com/";
    private static final int TIMEOUT = 30000;

    @Provides
    @Singleton
    GsonConverterFactory gsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    RxJava2CallAdapterFactory rxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    Cache provideCache(Application application) {
        int cacheSize = 5 * 1024 * 1024;
        File cacheDir = application.getCacheDir();
        return new Cache(cacheDir, cacheSize);
    }

    @Provides
    @Singleton
    MyInterceptor provideInterceptor(){
        return new MyInterceptor();
    }

    @Provides
    @Singleton
    OkHttpClient okHttpClient(MyInterceptor interceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(GsonConverterFactory gsonFactory, RxJava2CallAdapterFactory rxAdapter, OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(gsonFactory)
                .addCallAdapterFactory(rxAdapter)
                .build();
    }

    @Provides
    @Singleton
    static WebServiceAPI provideServiceApi(Retrofit retrofit){
        return retrofit.create(WebServiceAPI.class);
    }
}
