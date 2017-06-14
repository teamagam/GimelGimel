package com.teamagam.gimelgimel.domain.messages.entity.contents;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import java.util.Date;

/**
 * <p/>
 * An immutable data class representing a geographic location sample.
 * </p>
 * <p>A location can consist of a {@link PointGeometry} , timestamp,
 * and other information such as bearing, altitude and velocity.
 * </p>
 */
public class LocationSample {

  private PointGeometry mPoint;
  private long mTime;
  private String mProvider;
  private boolean mHasSpeed;
  private float mSpeed;
  private boolean mHasBearing;
  private float mBearing;
  private boolean mHasAccuracy;
  private float mAccuracy;

  public LocationSample(PointGeometry mPoint,
      long mTime,
      String mProvider,
      boolean mHasSpeed,
      float mSpeed,
      boolean mHasBearing,
      float mBearing,
      boolean mHasAccuracy,
      float mAccuracy) {
    this.mPoint = mPoint;
    this.mTime = mTime;
    this.mProvider = mProvider;
    this.mHasSpeed = mHasSpeed;
    this.mSpeed = mSpeed;
    this.mHasBearing = mHasBearing;
    this.mBearing = mBearing;
    this.mHasAccuracy = mHasAccuracy;
    this.mAccuracy = mAccuracy;
  }

  /**
   * Returns the name of the provider that generated this fix.
   *
   * @return the provider, or null if it has not been set
   */
  public String getProvider() {
    return mProvider;
  }

  public long getTime() {
    return mTime;
  }

  public PointGeometry getLocation() {
    return new PointGeometry(mPoint);
  }

  public boolean hasSpeed() {
    return mHasSpeed;
  }

  public float getSpeed() {
    return mSpeed;
  }

  public boolean hasBearing() {
    return mHasBearing;
  }

  public float getBearing() {
    return mBearing;
  }

  public boolean hasAccuracy() {
    return mHasAccuracy;
  }

  public float getAccuracy() {
    return mAccuracy;
  }

  public long getAgeMillis() {
    return System.currentTimeMillis() - mTime;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("Location[");
    s.append(mProvider);
    s.append(" ");
    s.append(mPoint);
    if (mHasAccuracy) {
      s.append(String.format(" acc=%.0f", mAccuracy));
    } else {
      s.append(" acc=???");
    }
    if (mTime == 0) {
      s.append(" t=?!?");
    } else {
      s.append(" t=");
      s.append(new Date(mTime));
    }

    if (mPoint.hasAltitude()) {
      s.append(" alt=").append(mPoint.getAltitude());
    }
    if (mHasSpeed) {
      s.append(" vel=").append(mSpeed);
    }
    if (mHasBearing) {
      s.append(" bear=").append(mBearing);
    }

    s.append(']');
    return s.toString();
  }
}
