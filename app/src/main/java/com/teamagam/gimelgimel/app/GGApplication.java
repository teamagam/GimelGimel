package com.teamagam.gimelgimel.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.ApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.components.DaggerApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
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

        mApplicationComponent.displayMyLocationOnMapInteractor().execute();
        mApplicationComponent.displayUserLocationsInteractor().execute();
        mApplicationComponent.displaySensorsOnMapInteractor().execute();
        mApplicationComponent.displayAlertOnMapInteractor().execute();
        mApplicationComponent.sendMyLocationInteractor().execute();

        // Initialize the fresco plugin.
        // Should be here instead of each activity
        Fresco.initialize(this);
    }

    private void initializeMessagePolling() {
        resetMessageSynchronizationTime();
        mApplicationComponent.startFetchingMessagesInteractor().execute();
    }

    private void initializeLoggers() {
        AppLoggerFactory.init(this);

        LoggerFactory.initialize(new LoggerFactory.Factory() {
            @Override
            public Logger create(String tag) {
                return AppLoggerFactory.create(tag);
            }
        });
    }

    private void resetMessageSynchronizationTime() {
        String latestReceivedDateKey = getResources().getString(
                R.string.pref_latest_received_message_date_in_ms);
        mApplicationComponent.userPreferencesRepository().setPreference(latestReceivedDateKey,
                (long) 0);
    }
}