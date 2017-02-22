package com.teamagam.gimelgimel.app.injectors.modules;


import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.rx.schedulers.DataThread;
import com.teamagam.gimelgimel.app.common.rx.schedulers.UIThread;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.functions.Action0;

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
    LocationFetcher provideLocationFetcher(final UIThread uiThread) {
        int minSamplingFrequency = mApplication.getResources().getInteger(
                R.integer.location_min_update_frequency_ms);
        int rapidSamplingFrequency = mApplication.getResources().getInteger(
                R.integer.location_rapid_update_frequency_ms);
        int minDistanceDelta = mApplication.getResources().getInteger(
                R.integer.location_threshold_update_distance_m);

        LocationFetcher.UiRunner uiRunner = new LocationFetcher.UiRunner() {
            @Override
            public void run(final Action0 action) {
                final Scheduler.Worker worker = uiThread.getScheduler().createWorker();

                worker.schedule(new Action0() {
                    @Override
                    public void call() {
                        action.call();
                        worker.unsubscribe();
                    }
                });
            }
        };

        return new LocationFetcher(
                mApplication,
                uiRunner,
                minSamplingFrequency,
                rapidSamplingFrequency,
                minDistanceDelta);
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
