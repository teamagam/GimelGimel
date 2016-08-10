package com.teamagam.gimelgimel.domain.geometries.entities;

import com.teamagam.gimelgimel.domain.geometries.entities.interfaces.IGeometryVisitor;

/**
 * Simple data-object for latitude/longitude location
 */
public class PointGeometry implements Geometry {

    public static final PointGeometry DEFAULT_POINT = new PointGeometry(0,0,0);

    public double latitude;
    public double longitude;
    public double altitude;
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
    public String toString() {
        if(hasAltitude) {
            return String.format("%.6f,%.6f, alt=%.6f", latitude, longitude, altitude);
        } else {
            return String.format("%.6f,%.6f", latitude, longitude);
        }
    }

    @Override
    public void accept(IGeometryVisitor visitor) {
        visitor.visit(this);
    }
}
