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

    public PointGeometry(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAltitude = 0.0f;
        this.mHasAltitude = false;
    }

    public PointGeometry(double latitude, double longitude, double altitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAltitude = altitude;
        this.mHasAltitude = true;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public void setAltitude(double altitude) {
        mAltitude = altitude;
        mHasAltitude = true;
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
