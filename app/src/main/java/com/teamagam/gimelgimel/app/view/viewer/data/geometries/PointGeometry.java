package com.teamagam.gimelgimel.app.view.viewer.data.geometries;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bar on 29-Feb-16.
 *
 * Simple data-object for latitude/longitude location
 */
public class PointGeometry implements Geometry{

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("altitude")
    public double altitude;

    public PointGeometry(double latitude, double longitude){
        this(latitude, longitude, 0);
    }

    public PointGeometry(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
}
