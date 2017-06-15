package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;
import android.location.LocationListener;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.rx.schedulers.DataThread;
import com.teamagam.gimelgimel.app.common.rx.schedulers.UIThread;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.map.esri.EsriSpatialEngine;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import javax.inject.Singleton;

@Module
public class ApplicationModule {

  private static final AppLogger sLogger = AppLoggerFactory.create(ApplicationModule.class);

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
    int minSamplingFrequency = Constants.LOCATION_MIN_UPDATE_FREQUENCY_MS;
    int rapidSamplingFrequency = Constants.LOCATION_RAPID_UPDATE_FREQUENCY_MS;
    int minDistanceDelta = Constants.LOCATION_THRESHOLD_UPDATE_DISTANCE_M;

    LocationFetcher.UiRunner uiRunner = action -> {
      final Scheduler.Worker worker = uiThread.getScheduler().createWorker();

      worker.schedule(() -> {
        try {
          action.run();
        } catch (Exception e) {
          sLogger.e("UiRunner encountered a problem:\n" + e.toString());
        }
        worker.dispose();
      });
    };

    return new LocationFetcher(mApplication, uiRunner, minSamplingFrequency, rapidSamplingFrequency,
        minDistanceDelta);
  }

  @Provides
  @Singleton
  LocationListener provideLocationListener(LocationFetcher locationFetcher) {
    return locationFetcher.getLocationListener();
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

  @Provides
  SpatialEngine provideSpatialEngine(EsriSpatialEngine esriSpatialEngine) {
    return esriSpatialEngine;
  }
}
