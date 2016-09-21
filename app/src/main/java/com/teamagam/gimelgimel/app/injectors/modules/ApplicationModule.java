package com.teamagam.gimelgimel.app.injectors.modules;


import android.content.Context;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.rx.schedulers.DataThread;
import com.teamagam.gimelgimel.app.common.rx.schedulers.UIThread;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.data.location.repository.GpsLocationProvider;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

    private final GGApplication mApplication;

    public ApplicationModule(GGApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    GpsLocationProvider provideGpsLocationProvider() {
        return LocationFetcher.getInstance(mApplication);
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(DataThread dataThread) {
        return dataThread;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread thread) {
        return thread;
    }

}
