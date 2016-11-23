package com.teamagam.gimelgimel.app;

import android.app.Application;
import android.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.ApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.components.DaggerApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.app.injectors.modules.PreferencesModule;
import com.teamagam.gimelgimel.app.utils.BasicStringSecurity;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;
import com.teamagam.gimelgimel.domain.base.logging.DomainLogger;
import com.teamagam.gimelgimel.domain.base.logging.DomainLoggerFactory;
import com.teamagam.gimelgimel.domain.base.logging.DomainLoggerFactoryHolder;
import com.teamagam.gimelgimel.app.model.ViewsModels.UsersLocationViewModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.data.message.poller.RepeatedBackoffMessagePolling;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

public class GGApplication extends Application {

    private char[] mPrefSecureKey = ("GGApplicationSecuredKey!!!").toCharArray();
    private ApplicationComponent mApplicationComponent;

    @Inject
    UserPreferencesRepository mUserPreferencesRepository;


    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .preferencesModule(new PreferencesModule(this, mPrefSecureKey))
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    private void init() {
        initializeInjector();
        initializeLoggers();
        initializeMessagePolling();

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
        String latestReceivedDateKey = getResources().getString(R.string.pref_latest_received_message_date_in_ms);

        mUserPreferencesRepository.setPreference(latestReceivedDateKey, (long) 0);
    }
}