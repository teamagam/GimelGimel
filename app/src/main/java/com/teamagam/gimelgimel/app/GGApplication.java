package com.teamagam.gimelgimel.app;

import android.app.Application;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.components.ApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.components.DaggerApplicationComponent;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
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
    mApplicationComponent.startFetchingMessagesInteractor().execute();
  }

  private void initializeLoggers() {
    AppLoggerFactory.init(mExternalDirProvider);

    LoggerFactory.initialize(AppLoggerFactory::create);
  }

  public void startSendingLocation() {
    mApplicationComponent.sendMyLocationInteractor().execute();
  }
}