package com.teamagam.gimelgimel.app.view.viewer.data.geometries;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Simple data-object for latitude/longitude location
 */
public class PointGeometry implements Geometry, Parcelable {

    public static final PointGeometry DEFAULT_POINT = new PointGeometry(0, 0, 0);

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("altitude")
    public double altitude;

    @SerializedName("hasAltitude")
    public boolean hasAltitude;

    /**
     * Copy constructor
     *
     * @param pg - {@link PointGeometry } to copy
     */
    public PointGeometry(PointGeometry pg) {
        this.latitude = pg.latitude;
        this.longitude = pg.longitude;
        this.altitude = pg.altitude;
        this.hasAltitude = pg.hasAltitude;
    }

    public PointGeometry(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = 0.0f;
        this.hasAltitude = false;
    }

    public PointGeometry(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.hasAltitude = true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(altitude);
        dest.writeByte((byte) (hasAltitude ? 1 : 0));
    }

    public static final Parcelable.Creator<PointGeometry> CREATOR = new Creator<PointGeometry>() {

        @Override
        public PointGeometry createFromParcel(Parcel source) {
            double lat = source.readDouble();
            double lng = source.readDouble();
            double alt = source.readDouble();
            boolean hasAlt = source.readByte() != 0;
            if (hasAlt) {
                return new PointGeometry(lat, lng, alt);
            } else {
                return new PointGeometry(lat, lng);
            }
        }

        @Override
        public PointGeometry[] newArray(int size) {
            return new PointGeometry[size];
        }
    };
}
