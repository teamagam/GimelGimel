package com.teamagam.gimelgimel.domain.timeplay;

public class GeoSnapshotTimer {

  private final GeoTimespanCalculator mGeoTimespanCalculator;
  private final int mIntervalCount;

  private long mCurrentSnapshotTime;
  private int mCurrentInterval;
  private long mDelta;
  private long mMaxTimeCache;
  private long mMinTimeCache;

  public GeoSnapshotTimer(GeoTimespanCalculator geoTimespanCalculator, int intervalCount) {
    mGeoTimespanCalculator = geoTimespanCalculator;
    mIntervalCount = intervalCount;
    mCurrentSnapshotTime = -1L;
    mCurrentInterval = -1;
    mMinTimeCache = -1L;
    mMaxTimeCache = -1L;
  }

  public long getNextSnapshotTime() {
    advanceTime();
    return mCurrentSnapshotTime;
  }

  private void advanceTime() {
    if (isUninitializedState()) {
      initialize();
    } else {
      advanceTimeByDelta();
    }
  }

  private void initialize() {
    mCurrentSnapshotTime = getMinTime();
    mCurrentInterval = 0;
    mDelta = calculateDelta();
  }

  private void advanceTimeByDelta() {
    mCurrentInterval = (mCurrentInterval + 1) % (mIntervalCount + 1);

    if (mCurrentInterval == 0) {
      mCurrentSnapshotTime = getMinTime();
    } else if (mCurrentInterval == mIntervalCount) {
      mCurrentSnapshotTime = getMaxTime();
    } else {
      mCurrentSnapshotTime += mDelta;
    }
  }

  private long getMinTime() {
    if (mMinTimeCache == -1L) {
      mMinTimeCache = mGeoTimespanCalculator.getMinimumGeoItemDate().getTime();
    }
    return mMinTimeCache;
  }

  private long calculateDelta() {
    return (long) ((getMaxTime() - getMinTime()) / (mIntervalCount * 1.0));
  }

  private long getMaxTime() {
    if (mMaxTimeCache == -1L) {
      mMaxTimeCache = mGeoTimespanCalculator.getMaximumGeoItemDate().getTime();
    }
    return mMaxTimeCache;
  }

  public boolean isUninitializedState() {
    return mCurrentSnapshotTime == -1L;
  }
}
