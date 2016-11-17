package com.teamagam.gimelgimel.app;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;

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
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

public class GGApplication extends Application {

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

    @Inject
    UserPreferencesRepository mUserPreferencesRepository;


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

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
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

    private void init() {
        mApplicationComponent.inject(this);

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
        String latestReceivedDateKey = getResources().getString(R.string.pref_latest_received_message_date_in_ms);

        mUserPreferencesRepository.setPreference(latestReceivedDateKey, 0);
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