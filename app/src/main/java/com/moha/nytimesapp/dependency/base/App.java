package com.moha.nytimesapp.dependency.base;

import android.support.multidex.MultiDexApplication;

import com.moha.nytimesapp.BuildConfig;
import com.moha.nytimesapp.dependency.component.AppComponent;
import com.moha.nytimesapp.dependency.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class App extends MultiDexApplication implements HasAndroidInjector {
    public static final String API_KEY = BuildConfig.ApiKey;

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;
    private AppComponent mComponent;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerAppComponent.builder().application(this).build();
        mComponent.inject(this);
    }
}
