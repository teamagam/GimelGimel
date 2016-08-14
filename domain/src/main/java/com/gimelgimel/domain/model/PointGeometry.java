package com.gimelgimel.domain.model;

public class PointGeometry {

    public static final PointGeometry DEFAULT_POINT = new PointGeometry(0, 0, 0);

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

    @Override
    public String toString() {
        if (mHasAltitude) {
            return String.format("%.6f,%.6f, alt=%.6f", mLatitude, mLongitude, mAltitude);
        } else {
            return String.format("%.6f,%.6f", mLatitude, mLongitude);
        }
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
}
