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

public class GGApplication extends Application {

    private SecuredPreferenceUtil mPrefs;
    private char[] mPrefSecureKey = ("GGApplicationSecuredKey!!!").toCharArray();
    private ApplicationComponent mApplicationComponent;


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

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public SecuredPreferenceUtil getPrefs() {
        if (mPrefs == null) {
            // Set up a preferences manager (with basic security)
            mPrefs = new SecuredPreferenceUtil(getResources(),
                    PreferenceManager.getDefaultSharedPreferences(this),
                    new BasicStringSecurity(mPrefSecureKey));

            loadDefaultXmlValues(R.xml.pref_general);
            loadDefaultXmlValues(R.xml.pref_mesages);
        }

        return mPrefs;
    }

    private void loadDefaultXmlValues(int xmlId) {
        PreferenceManager.setDefaultValues(this, xmlId, false);
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
        getPrefs().applyLong(R.string.pref_latest_received_message_date_in_ms, 0);
    }
}