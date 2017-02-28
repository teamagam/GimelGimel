package com.teamagam.gimelgimel.app;

import android.app.Application;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.ApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.components.DaggerApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;

public class GGApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void init() {
        initializeInjector();
        initializeLoggers();
        initializeMessagePolling();

        mApplicationComponent.displayUserLocationsInteractor().execute();
        mApplicationComponent.displaySensorsOnMapInteractor().execute();
        mApplicationComponent.sendMyLocationInteractor().execute();
    }

    private void initializeMessagePolling() {
        resetMessageSynchronizationTime();
        mApplicationComponent.startFetchingMessagesInteractor().execute();
    }

    private void initializeLoggers() {
        AppLoggerFactory.init(this);

        LoggerFactory.initialize(AppLoggerFactory::create);
    }

    private void resetMessageSynchronizationTime() {
        String latestReceivedDateKey = getResources().getString(
                R.string.pref_latest_received_message_date_in_ms);
        mApplicationComponent.userPreferencesRepository().setPreference(latestReceivedDateKey,
                (long) 0);
    }
}