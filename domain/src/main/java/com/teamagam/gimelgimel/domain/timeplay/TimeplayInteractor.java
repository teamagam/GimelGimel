package com.teamagam.gimelgimel.domain.timeplay;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.Observable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AutoFactory
public class TimeplayInteractor extends BaseSingleDisplayInteractor {

  private static final int DISPLAY_INTERVAL_MILLIS = 2 * 1000;
  private final GeoSnapshoter mGeoSnapshoter;
  private final Displayer mDisplayer;
  private final GeoSnapshotTimer mGeoSnapshotTimer;
  private List<GeoEntity> mDisplayedGeoEntities;

  public TimeplayInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided GeoSnapshoter geoSnapshoter,
      @Provided GeoTimespanCalculator geoTimespanCalculator,
      Displayer displayer,
      int intervalCount) {
    super(threadExecutor, postExecutionThread);
    mGeoSnapshoter = geoSnapshoter;
    mGeoSnapshotTimer = new GeoSnapshotTimer(geoTimespanCalculator, intervalCount);
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(
        Observable.interval((long) DISPLAY_INTERVAL_MILLIS, TimeUnit.MILLISECONDS),
        tickObservables -> tickObservables.map(tick -> getNextSnapshot()), this::display);
  }

  private List<GeoEntity> getNextSnapshot() {
    return mGeoSnapshoter.snapshot(mGeoSnapshotTimer.getNextSnapshotTime());
  }

  private void display(List<GeoEntity> snapshot) {
    removeOldEntities();
    displayNewEntities(snapshot);
  }

  private void removeOldEntities() {
    if (mDisplayedGeoEntities != null) {
      for (GeoEntity entity : mDisplayedGeoEntities) {
        mDisplayer.removeFromMap(entity);
      }
    }
  }

  private void displayNewEntities(List<GeoEntity> snapshot) {
    for (GeoEntity entity : snapshot) {
      mDisplayer.addToMap(entity);
    }
    mDisplayedGeoEntities = snapshot;
  }

  public interface Displayer {
    void addToMap(GeoEntity geoEntity);

    void removeFromMap(GeoEntity geoEntity);
  }
}
