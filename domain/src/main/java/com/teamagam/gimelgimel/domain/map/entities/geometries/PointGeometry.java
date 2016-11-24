package com.teamagam.gimelgimel.domain.map.entities.geometries;

import com.teamagam.gimelgimel.domain.map.entities.interfaces.IGeometryVisitor;

public class PointGeometry implements Geometry {

    private double mLatitude;
    private double mLongitude;
    private double mAltitude;
    private boolean mHasAltitude;

    /**
     * Copy constructor
     *
     * @param pg - {@link PointGeometry } to copy
     */
    public PointGeometry(PointGeometry pg) {
        this.mLatitude = pg.mLatitude;
        this.mLongitude = pg.mLongitude;
        this.mAltitude = pg.mAltitude;
        this.mHasAltitude = pg.mHasAltitude;
    }

    /**
     * constructor without altitude
     * @param latitude
     * @param longitude
     */
    public PointGeometry(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAltitude = 0.0f;
        this.mHasAltitude = false;
    }

    /**
     * constructor with altitude
     * @param latitude
     * @param longitude
     * @param altitude
     */
    public PointGeometry(double latitude, double longitude, double altitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAltitude = altitude;
        this.mHasAltitude = true;
    }

    /**
     * constructor with direct boolean that represents if there is an altitude.
     * @param latitude
     * @param longitude
     * @param altitude
     */
    public PointGeometry(double latitude, double longitude, boolean hasAltitude, double altitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mHasAltitude = hasAltitude;
        this.mAltitude = altitude;
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
    public void accept(IGeometryVisitor visitor) {
        visitor.visit(this);
    }
}
