package com.moha.nytimesapp.dependency.module;

import com.moha.nytimesapp.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivityInjector();

}
