package com.teamagam.gimelgimel.domain.map.entities.geometries;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.GeometryVisitor;

public class PointGeometry implements Geometry {
  private double mLatitude;
  private double mLongitude;
  private double mAltitude;
  private boolean mHasAltitude;
  public PointGeometry(PointGeometry pg) {
    this.mLatitude = pg.mLatitude;
    this.mLongitude = pg.mLongitude;
    this.mAltitude = pg.mAltitude;
    this.mHasAltitude = pg.mHasAltitude;
  }
  public PointGeometry(double latitude, double longitude) {
    this(latitude, longitude, false, 0.0f);
  }

  public PointGeometry(double latitude, double longitude, double altitude) {
    this(latitude, longitude, true, altitude);
  }

  public PointGeometry(double latitude, double longitude, boolean hasAltitude, double altitude) {
    this.mLatitude = latitude;
    this.mLongitude = longitude;
    this.mHasAltitude = hasAltitude;
    this.mAltitude = altitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PointGeometry that = (PointGeometry) o;

    if (Double.compare(that.mLatitude, mLatitude) != 0) return false;
    if (Double.compare(that.mLongitude, mLongitude) != 0) return false;
    if (Double.compare(that.mAltitude, mAltitude) != 0) return false;
    return mHasAltitude == that.mHasAltitude;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    temp = Double.doubleToLongBits(mLatitude);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(mLongitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(mAltitude);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (mHasAltitude ? 1 : 0);
    return result;
  }

  public double getLatitude() {
    return mLatitude;
  }

  public double getLongitude() {
    return mLongitude;
  }

  public double getAltitude() {
    return mAltitude;
  }

  public boolean hasAltitude() {
    return this.mHasAltitude;
  }

  @Override
  public void accept(GeometryVisitor visitor) {
    visitor.visit(this);
  }
}
