package com.teamagam.gimelgimel.app.view.viewer.data.geometries;

/**
 * Simple data-object for latitude/longitude location
 */
public class PointGeometry implements Geometry {

    public static final PointGeometry DEFAULT_POINT = new PointGeometry(0,0,0);

    public double latitude;
    public double longitude;
    public double altitude;

    /**
     * Copy constructor
     *
     * @param pg - {@link PointGeometry } to copy
     */
    public PointGeometry(PointGeometry pg) {
        this.latitude = pg.latitude;
        this.longitude = pg.longitude;
        this.altitude = pg.altitude;
    }

    public PointGeometry(double latitude, double longitude) {
        this(latitude, longitude, 0);
    }

    public PointGeometry(double latitude, double longitude, double altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
}
