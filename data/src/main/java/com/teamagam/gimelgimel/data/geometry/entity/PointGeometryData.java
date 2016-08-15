package com.teamagam.gimelgimel.data.geometry.entity;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.domain.geometries.entities.Geometry;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;

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

    @Override
    public String toString() {
        if(hasAltitude) {
            return String.format("%.6f,%.6f, alt=%.6f", latitude, longitude, altitude);
        } else {
            return String.format("%.6f,%.6f", latitude, longitude);
        }
    }

    @Override
    public Geometry transformToEntity() {
        if(hasAltitude) {
            return new PointGeometry( latitude, longitude, altitude);
        } else {
            return new PointGeometry(latitude, longitude);
        }
    }
}
