package com.teamagam.gimelgimel.domain.timeplay;

public class GeoSnapshotTimer {

  private final GeoTimespanCalculator mGeoTimespanCalculator;
  private final int mIntervalCount;
  private long mInitialTimestamp;

  private long mCurrentSnapshotTime;
  private long mDelta;
  private long mMaxTimeCache;
  private long mMinTimeCache;

  public GeoSnapshotTimer(GeoTimespanCalculator geoTimespanCalculator,
      int intervalCount,
      long initialTimestamp) {
    mGeoTimespanCalculator = geoTimespanCalculator;
    mIntervalCount = intervalCount;
    mInitialTimestamp = initialTimestamp;
    mCurrentSnapshotTime = -1L;
    mMinTimeCache = -1L;
    mMaxTimeCache = -1L;
  }

  public long getNextSnapshotTime() {
    advanceTime();
    return mCurrentSnapshotTime;
  }

  public long getMinTime() {
    if (mMinTimeCache == -1L) {
      mMinTimeCache = mGeoTimespanCalculator.getMinimumGeoItemDate().getTime();
    }
    return mMinTimeCache;
  }

  public long getMaxTime() {
    if (mMaxTimeCache == -1L) {
      mMaxTimeCache = mGeoTimespanCalculator.getMaximumGeoItemDate().getTime();
    }
    return mMaxTimeCache;
  }

  private void advanceTime() {
    if (isUninitializedState()) {
      initialize();
    } else {
      advanceTimeByDeltaCyclic();
    }
  }

  private boolean isUninitializedState() {
    return mCurrentSnapshotTime == -1L;
  }

  private void initialize() {
    mCurrentSnapshotTime = Math.max(mInitialTimestamp, getMinTime());
    mDelta = calculateDelta();
  }

  private long calculateDelta() {
    return (long) ((getMaxTime() - getMinTime()) / (mIntervalCount * 1.0));
  }

  private void advanceTimeByDeltaCyclic() {
    mCurrentSnapshotTime += mDelta;
    if (isExceedingMaxBeforeCycle()) {
      mCurrentSnapshotTime = getMaxTime();
    }
    if (shouldCycle()) {
      mCurrentSnapshotTime = getMinTime();
    }
  }

  private boolean isExceedingMaxBeforeCycle() {
    return mCurrentSnapshotTime > getMaxTime() && mCurrentSnapshotTime < getMaxTime() + mDelta;
  }

  private boolean shouldCycle() {
    return mCurrentSnapshotTime == getMaxTime() + mDelta;
  }
}
