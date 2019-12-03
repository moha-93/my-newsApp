package com.moha.nytimesapp.dependency.module;

import com.moha.nytimesapp.fragment.MEmailedFragment;
import com.moha.nytimesapp.fragment.MSharedFragment;
import com.moha.nytimesapp.fragment.MViewedFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract MEmailedFragment bindEFragment();

    @ContributesAndroidInjector
    abstract MSharedFragment bindSFragment();

    @ContributesAndroidInjector
    abstract MViewedFragment bindVFragment();
}

