package com.teamagam.gimelgimel.app;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.RepeatedBackoffTaskRunner;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.ApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.components.DaggerApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.app.injectors.modules.PreferencesModule;
import com.teamagam.gimelgimel.app.message.viewModel.MessagesMasterViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.UsersLocationViewModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.app.network.services.message_polling.RepeatedBackoffMessagePolling;
import com.teamagam.gimelgimel.app.utils.BasicStringSecurity;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;

public class GGApplication extends Application {

    private SecuredPreferenceUtil mPrefs;
    private char[] mPrefSecureKey = ("GGApplicationSecuredKey!!!").toCharArray();
    private RepeatedBackoffMessagePolling mRepeatedBackoffMessagePolling;
    private MessagesModel mMessagesModel;
    private MessagesReadStatusModel mMessagesReadStatusModel;
    private SelectedMessageModel mSelectedMessageModel;
    private UsersLocationViewModel mUserLocationViewModel;
    private GGMessageSender mGGMessageSender;
    private Handler mSharedBackgroundHandler;
    private Handler mMessagingHandler;

    private ApplicationComponent mApplicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        initializeInjector();

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

    public RepeatedBackoffTaskRunner getRepeatedBackoffMessagePolling() {
        return mRepeatedBackoffMessagePolling;
    }


    public UsersLocationViewModel getUserLocationViewModel() {
        return mUserLocationViewModel;
    }

    public MessagesModel getMessagesModel() {
        return mMessagesModel;
    }

    public Handler getSharedBackgroundHandler() {
        return mSharedBackgroundHandler;
    }

    public Handler getMessagingHandler() {
        return mMessagingHandler;
    }

    public GGMessageSender getMessageSender() {
        return mGGMessageSender;
    }

    private void loadDefaultXmlValues(int xmlId) {
        PreferenceManager.setDefaultValues(this, xmlId, false);
    }

    private void init() {
        compositeModels();


        mSharedBackgroundHandler = createHandlerThread("backgroundThread");

        resetMessageSynchronizationTime();

        mApplicationComponent.startFetchingMessagesInteractor().execute();

        mGGMessageSender = new GGMessageSender(this);

        LoggerFactory.init(this);

        // Initialize the fresco plugin.
        // Should be here instead of each activity
        Fresco.initialize(this);
    }


    private void resetMessageSynchronizationTime() {
        getPrefs().applyLong(R.string.pref_latest_received_message_date_in_ms, 0);
    }

    private Handler createHandlerThread(String name) {
        HandlerThread ht = new HandlerThread(name);
        ht.start();
        return new Handler(ht.getLooper());
    }


    private void compositeModels() {
        mMessagesModel = new InMemoryMessagesModel();
        mMessagesReadStatusModel = new InMemoryMessagesReadStatusModel();
        mSelectedMessageModel = new InMemorySelectedMessageModel();
    }
}