package com.teamagam.gimelgimel.app.map.model.geometries;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Simple data-object for latitude/longitude location
 */
public class PointGeometryApp implements GeometryApp, Parcelable {

    public static final PointGeometryApp DEFAULT_POINT = new PointGeometryApp(0,0,0);

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
     * @param pg - {@link PointGeometryApp } to copy
     */
    public PointGeometryApp(PointGeometryApp pg) {
        this.latitude = pg.latitude;
        this.longitude = pg.longitude;
        this.altitude = pg.altitude;
        this.hasAltitude = pg.hasAltitude;
    }

    public PointGeometryApp(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = 0.0f;
        this.hasAltitude = false;
    }

    public PointGeometryApp(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.hasAltitude = true;
    }

    @Override
    public String toString() {
        if(hasAltitude) {
            return String.format("%.6f,%.6f, alt=%.6f", latitude, longitude, altitude);
        } else {
            return String.format("%.6f,%.6f", latitude, longitude);
        }
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

    protected PointGeometryApp(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        altitude = in.readDouble();
        hasAltitude = in.readByte() != 0;
    }

    public static final Creator<PointGeometryApp> CREATOR = new Creator<PointGeometryApp>() {
        @Override
        public PointGeometryApp createFromParcel(Parcel in) {
            return new PointGeometryApp(in);
        }

        @Override
        public PointGeometryApp[] newArray(int size) {
            return new PointGeometryApp[size];
        }
    };

}
