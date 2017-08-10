package com.teamagam.gimelgimel.domain.timeplay;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@AutoFactory
public class TimeplayInteractor extends BaseSingleDisplayInteractor {

  private static final Logger sLogger =
      LoggerFactory.create(TimeplayInteractor.class.getSimpleName());
  private static final int DISPLAY_INTERVAL_MILLIS = 100;

  private final GeoSnapshoter mGeoSnapshoter;
  private final Displayer mDisplayer;
  private final GeoSnapshotTimer mGeoSnapshotTimer;
  private final Set<GeoEntity> mDisplayedGeoEntities;
  private boolean mIsFirstDisplaying;

  public TimeplayInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided GeoSnapshoter geoSnapshoter,
      @Provided GeoTimespanCalculator geoTimespanCalculator,
      Displayer displayer, int intervalCount, long initialTimestamp) {
    super(threadExecutor, postExecutionThread);
    mGeoSnapshoter = geoSnapshoter;
    mGeoSnapshotTimer =
        new GeoSnapshotTimer(geoTimespanCalculator, intervalCount, initialTimestamp);
    mDisplayer = displayer;
    mIsFirstDisplaying = true;
    mDisplayedGeoEntities = new LinkedHashSet<>();
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(
        Observable.interval(0, (long) DISPLAY_INTERVAL_MILLIS, TimeUnit.MILLISECONDS),
        tickObservables -> tickObservables.map(tick -> getNextSnapshot()), this::display);
  }

  private TimeplaySnapshot getNextSnapshot() {
    long timestamp = mGeoSnapshotTimer.getNextSnapshotTime();
    sLogger.d("Snapshoting geo entities with timestamp " + timestamp);
    List<GeoEntity> snapshot = mGeoSnapshoter.snapshot(timestamp);
    return new TimeplaySnapshot(snapshot, timestamp);
  }

  private void display(TimeplaySnapshot snapshot) {
    sLogger.d("display snapshot of size " + snapshot.getGeoEntities().size());

    if (mIsFirstDisplaying) {
      setMinMaxTimestamps();
      mIsFirstDisplaying = false;
    }

    removeGoneEntities(snapshot.getGeoEntities());
    displayNewEntities(snapshot.getGeoEntities());

    mDisplayer.displayTimestamp(snapshot.getTimestamp());
  }

  private void setMinMaxTimestamps() {
    mDisplayer.setTimespan(mGeoSnapshotTimer.getMinTime(), mGeoSnapshotTimer.getMaxTime());
  }

  private void removeGoneEntities(List<GeoEntity> newEntities) {
    for (GeoEntity entity : getGoneEntities(newEntities)) {
      hide(entity);
    }
  }

  private List<GeoEntity> getGoneEntities(List<GeoEntity> newSnapshotEntities) {
    List<GeoEntity> res = new ArrayList<>(newSnapshotEntities.size());
    for (GeoEntity entity : mDisplayedGeoEntities) {
      if (!newSnapshotEntities.contains(entity)) {
        res.add(entity);
      }
    }
    return res;
  }

  private void hide(GeoEntity entity) {
    mDisplayer.removeFromMap(entity);
    mDisplayedGeoEntities.remove(entity);
  }

  private void displayNewEntities(List<GeoEntity> snapshot) {
    for (GeoEntity entity : snapshot) {
      if (!isDisplayed(entity)) {
        show(entity);
      }
    }
  }

  private boolean isDisplayed(GeoEntity entity) {
    return mDisplayedGeoEntities.contains(entity);
  }

  private void show(GeoEntity entity) {
    mDisplayer.addToMap(entity);
    mDisplayedGeoEntities.add(entity);
  }

  public interface Displayer {

    void displayTimestamp(long timestamp);

    void setTimespan(long startTimestamp, long endTimestamp);

    void addToMap(GeoEntity geoEntity);

    void removeFromMap(GeoEntity geoEntity);
  }

  private static class TimeplaySnapshot {
    private final List<GeoEntity> mGeoEntities;
    private final long mTimestamp;

    public TimeplaySnapshot(List<GeoEntity> geoEntities, long timestamp) {
      mGeoEntities = geoEntities;
      mTimestamp = timestamp;
    }

    public List<GeoEntity> getGeoEntities() {
      return mGeoEntities;
    }

    public long getTimestamp() {
      return mTimestamp;
    }
  }
}
