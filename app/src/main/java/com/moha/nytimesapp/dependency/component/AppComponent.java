package com.moha.nytimesapp.dependency.component;

import android.app.Application;

import com.moha.nytimesapp.dependency.base.App;
import com.moha.nytimesapp.dependency.module.ActivityModule;
import com.moha.nytimesapp.dependency.module.FragmentModule;
import com.moha.nytimesapp.dependency.module.ServiceModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {ServiceModule.class, AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class})
public interface AppComponent extends AndroidInjector<App> {
    void inject(App app);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

}
