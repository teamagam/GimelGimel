package com.teamagam.gimelgimel.app;

import android.app.Application;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.ApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.components.DaggerApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import javax.inject.Inject;

public class GGApplication extends Application {

  @Inject
  ExternalDirProvider mExternalDirProvider;

  private ApplicationComponent mApplicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    init();
  }

  public ApplicationComponent getApplicationComponent() {
    return mApplicationComponent;
  }

  private void init() {
    initializeInjector();
    initializeLoggers();
    initializeMessagePolling();
  }

  private void initializeInjector() {
    mApplicationComponent =
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    mApplicationComponent.inject(this);
  }

  private void initializeMessagePolling() {
    initMessageSynchronizationTime();
    mApplicationComponent.startFetchingMessagesInteractor().execute();
  }

  private void initMessageSynchronizationTime() {
    String latestReceivedDateKey =
        getResources().getString(R.string.pref_latest_received_message_date_in_ms);
    UserPreferencesRepository preferencesRepository =
        mApplicationComponent.userPreferencesRepository();

    if (!preferencesRepository.contains(latestReceivedDateKey)) {
      preferencesRepository.setPreference(latestReceivedDateKey, 0L);
    }
  }

  private void initializeLoggers() {
    AppLoggerFactory.init(mExternalDirProvider);

    LoggerFactory.initialize(AppLoggerFactory::create);
  }

  public void startSendingLocation() {
    mApplicationComponent.sendMyLocationInteractor().execute();
  }
}