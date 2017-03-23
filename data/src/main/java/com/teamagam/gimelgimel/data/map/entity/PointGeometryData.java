package com.teamagam.gimelgimel.data.map.entity;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

/**
 * Simple data-object for latitude/longitude location
 */
public class PointGeometryData implements GeometryData {

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
     * @param pg - {@link PointGeometryData } to copy
     */
    public PointGeometryData(PointGeometryData pg) {
        this.latitude = pg.latitude;
        this.longitude = pg.longitude;
        this.altitude = pg.altitude;
        this.hasAltitude = pg.hasAltitude;
    }

    public PointGeometryData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = 0.0f;
        this.hasAltitude = false;
    }

    public PointGeometryData(double latitude, double longitude, double altitude) {
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
    public PointGeometry toModel() {
        if(hasAltitude) {
            return new PointGeometry(latitude, longitude, altitude);
        } else {
            return new PointGeometry(latitude, longitude);
        }
    }
}
