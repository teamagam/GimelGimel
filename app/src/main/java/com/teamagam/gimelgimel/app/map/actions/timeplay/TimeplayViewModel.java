package com.teamagam.gimelgimel.app.map.actions.timeplay;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.AutoTimeplayInteractorFactory;
import com.teamagam.gimelgimel.domain.timeplay.SnapshotTimeplayInteractor;
import com.teamagam.gimelgimel.domain.timeplay.SnapshotTimeplayInteractorFactory;
import com.teamagam.gimelgimel.domain.timeplay.TimeplayInteractor;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AutoFactory
public class TimeplayViewModel extends BaseViewModel {

  private static final int INTERVAL_COUNT = 100;

  private final AutoTimeplayInteractorFactory mAutoTimeplayInteractorFactory;
  private final SnapshotTimeplayInteractorFactory mSnapshotTimeplayInteractorFactory;
  private final MapDisplayer mMapDisplayer;
  private final DateFormat mDateFormat;
  private final DateFormat mTimeFormat;
  private final Set<GeoEntity> mDisplayed;
  private TimeplayInteractor mDisplayInteractor;
  private Date mCurrentDisplayedDate;
  private boolean mIsPlaying;
  private long mStartTimestamp;
  private long mEndTimestamp;
  private TimeplayDisplayer mTimeplayDisplayer;
  private SnapshotTimeplayInteractor mSnapshotInteractor;

  public TimeplayViewModel(@Provided AutoTimeplayInteractorFactory autoTimeplayInteractorFactory,
      @Provided SnapshotTimeplayInteractorFactory snapshotTimeplayInteractorFactory,
      DateFormat dateFormat,
      DateFormat timeFormat,
      MapDisplayer mapDisplayer) {
    mAutoTimeplayInteractorFactory = autoTimeplayInteractorFactory;
    mSnapshotTimeplayInteractorFactory = snapshotTimeplayInteractorFactory;
    mDateFormat = dateFormat;
    mTimeFormat = timeFormat;
    mMapDisplayer = mapDisplayer;
    mTimeplayDisplayer = new TimeplayDisplayer();
    mDisplayed = new HashSet<>();
    mCurrentDisplayedDate = null;
    mIsPlaying = false;
    mStartTimestamp = -1;
    mEndTimestamp = -1;
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void stop() {
    super.stop();
  }

  public String getFormattedDate() {
    return format(mDateFormat, "-");
  }

  public String getFormattedTime() {
    return format(mTimeFormat, "-");
  }

  public int getPlayOrResumeDrawableId() {
    return mIsPlaying ? R.drawable.ic_pause : R.drawable.ic_play;
  }

  public int getProgress() {
    if (hasNotStarted()) {
      return 0;
    }
    return getTimelineProgressPercentage();
  }

  public void onPlayResumeClicked() {
    if (mIsPlaying) {
      pause();
    } else {
      play();
    }
    updateUi();
  }

  public void onProgressBarUserChange(double normalizedProgress) {
    pause();
    long newTimestamp = (long) (mStartTimestamp + normalizedProgress * getTotalTimespan());
    mCurrentDisplayedDate = new Date(newTimestamp);
    showSnapshot(newTimestamp);
    updateUi();
  }

  private String format(DateFormat dateFormat, String defaultValue) {
    if (mCurrentDisplayedDate == null) {
      return defaultValue;
    }
    return dateFormat.format(mCurrentDisplayedDate);
  }

  private boolean hasNotStarted() {
    return mCurrentDisplayedDate == null || mStartTimestamp == -1 || mEndTimestamp == -1;
  }

  private int getTimelineProgressPercentage() {
    long currentDelta = mCurrentDisplayedDate.getTime() - mStartTimestamp;
    double proportion = (currentDelta * 1.0) / getTotalTimespan();
    return (int) Math.ceil(proportion * 100);
  }

  private long getTotalTimespan() {
    return mEndTimestamp - mStartTimestamp;
  }

  private void pause() {
    unsubscribe(mDisplayInteractor);
    mIsPlaying = false;
  }

  private void play() {
    clearMap();
    mDisplayInteractor = createAutoDisplayInteractor();
    execute(mDisplayInteractor);
    mIsPlaying = true;
  }

  private void clearMap() {
    for (GeoEntity entity : mDisplayed) {
      mMapDisplayer.removeFromMap(entity);
    }
  }

  private TimeplayInteractor createAutoDisplayInteractor() {
    return mAutoTimeplayInteractorFactory.create(mTimeplayDisplayer, INTERVAL_COUNT,
        getInitialTimestamp());
  }

  private long getInitialTimestamp() {
    return mCurrentDisplayedDate == null ? 0 : mCurrentDisplayedDate.getTime();
  }

  private void updateUi() {
    notifyPropertyChanged(BR._all);
  }

  private void showSnapshot(long newTimestamp) {
    clearMap();
    stopPreviousSnapshotDisplay();
    startCurrentSnapshotDisplay(newTimestamp);
  }

  private void stopPreviousSnapshotDisplay() {
    if (mSnapshotInteractor != null) {
      mSnapshotInteractor.unsubscribe();
    }
  }

  private void startCurrentSnapshotDisplay(long newTimestamp) {
    mSnapshotInteractor =
        mSnapshotTimeplayInteractorFactory.create(mTimeplayDisplayer, newTimestamp);
    mSnapshotInteractor.execute();
  }

  interface MapDisplayer {
    void addToMap(GeoEntity geoEntity);

    void removeFromMap(GeoEntity geoEntity);
  }

  private class TimeplayDisplayer implements TimeplayInteractor.Displayer {
    @Override
    public void displayTimestamp(long timestamp) {
      mCurrentDisplayedDate = new Date(timestamp);
      updateUi();
    }

    @Override
    public void setTimespan(long startTimestamp, long endTimestamp) {
      mStartTimestamp = startTimestamp;
      mEndTimestamp = endTimestamp;
    }

    @Override
    public void addToMap(GeoEntity geoEntity) {
      mMapDisplayer.addToMap(geoEntity);
      mDisplayed.add(geoEntity);
    }

    @Override
    public void removeFromMap(GeoEntity geoEntity) {
      mMapDisplayer.removeFromMap(geoEntity);
      mDisplayed.remove(geoEntity);
    }
  }
}
