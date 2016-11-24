package com.teamagam.gimelgimel.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.ApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.components.DaggerApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.domain.base.logging.DomainLogger;
import com.teamagam.gimelgimel.domain.base.logging.DomainLoggerFactory;
import com.teamagam.gimelgimel.domain.base.logging.DomainLoggerFactoryHolder;
import com.teamagam.gimelgimel.domain.map.DisplayMyLocationOnMapInteractor;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

public class GGApplication extends Application {

    private char[] mPrefSecureKey = ("GGApplicationSecuredKey!!!").toCharArray();
    private ApplicationComponent mApplicationComponent;

    @Inject
    UserPreferencesRepository mUserPreferencesRepository;

    @Inject
    DisplayMyLocationOnMapInteractor mDisplayMyLocationOnMapInteractor;


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
        mDisplayMyLocationOnMapInteractor.execute();

        // Initialize the fresco plugin.
        // Should be here instead of each activity
        Fresco.initialize(this);
    }

    private void initializeMessagePolling() {
        resetMessageSynchronizationTime();
        mApplicationComponent.startFetchingMessagesInteractor().execute();
    }

    private void initializeLoggers() {
        LoggerFactory.init(this);

        DomainLoggerFactoryHolder.initialize(new DomainLoggerFactory() {
            @Override
            public DomainLogger create(String tag) {
                return LoggerFactory.create(tag);
            }
        });
    }

    private void resetMessageSynchronizationTime() {
        String latestReceivedDateKey = getResources().getString(
                R.string.pref_latest_received_message_date_in_ms);

        mUserPreferencesRepository.setPreference(latestReceivedDateKey, (long) 0);
    }
}