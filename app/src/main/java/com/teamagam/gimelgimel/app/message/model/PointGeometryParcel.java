package com.teamagam.gimelgimel.app.message.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public class PointGeometryParcel implements Parcelable {

  public static final Creator<PointGeometryParcel> CREATOR = new Creator<PointGeometryParcel>() {
    @Override
    public PointGeometryParcel createFromParcel(Parcel in) {
      return new PointGeometryParcel(in);
    }

    @Override
    public PointGeometryParcel[] newArray(int size) {
      return new PointGeometryParcel[size];
    }
  };
  private double mLatitude;
  private double mLongitude;
  private double mAltitude;
  private boolean mHasAltitude;
  private PointGeometryParcel(double latitude,
      double longitude,
      boolean hasAltitude,
      double altitude) {
    mLatitude = latitude;
    mLongitude = longitude;
    mAltitude = altitude;
    mHasAltitude = hasAltitude;
  }

  private PointGeometryParcel(Parcel in) {
    mLatitude = in.readDouble();
    mLongitude = in.readDouble();
    mAltitude = in.readDouble();
    mHasAltitude = in.readByte() != 0;
  }

  public static PointGeometryParcel create(PointGeometry pg) {
    return new PointGeometryParcel(pg.getLatitude(), pg.getLongitude(), pg.hasAltitude(),
        pg.getAltitude());
  }

  public PointGeometry convert() {
    return new PointGeometry(mLatitude, mLongitude, mHasAltitude, mAltitude);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeDouble(mLatitude);
    dest.writeDouble(mLongitude);
    dest.writeDouble(mAltitude);
    dest.writeByte((byte) (mHasAltitude ? 1 : 0));
  }
}
