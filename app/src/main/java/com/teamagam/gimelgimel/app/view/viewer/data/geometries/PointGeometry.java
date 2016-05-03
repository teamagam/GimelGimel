package com.teamagam.gimelgimel.app.view.viewer.data.geometries;

import com.google.gson.annotations.SerializedName;

/**
 * Simple data-object for latitude/longitude location
 */
public class PointGeometry implements Geometry {

    public static final PointGeometry DEFAULT_POINT = new PointGeometry(0,0,0);

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
}
