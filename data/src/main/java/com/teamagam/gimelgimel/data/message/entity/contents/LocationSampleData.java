package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.geometry.entity.PointGeometryData;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

/**
 * Created on 4/18/2016.
 * <p/>
 * An immutable data class representing a geographic location sample.
 * <p/>
 * <p>A location can consist of a {@link PointGeometryData} , timestamp,
 * and other information such as bearing, altitude and velocity.
 */
public class LocationSampleData {

    @SerializedName("location")
    private PointGeometryData mPoint;

    @SerializedName("timeStamp")
    private long mTime;

    @SerializedName("provider")
    private String mProvider;

    @SerializedName("hasSpeed")
    private boolean mHasSpeed = false;

    @SerializedName("speed")
    private float mSpeed = 0.0f;

    @SerializedName("hasBearing")
    private boolean mHasBearing = false;

    @SerializedName("bearing")
    private float mBearing = 0.0f;

    @SerializedName("hasAccuracy")
    private boolean mHasAccuracy = false;

    @SerializedName("accuracy")
    private float mAccuracy = 0.0f;

    public LocationSampleData(LocationSampleEntity locationSampleEntity, PointGeometryData point) {
        mPoint = point;
        mTime = locationSampleEntity.getTime();
        mProvider = locationSampleEntity.getProvider();

        mSpeed = locationSampleEntity.getSpeed();
        mHasSpeed = locationSampleEntity.hasSpeed();

        mBearing = locationSampleEntity.getBearing();
        mHasBearing = locationSampleEntity.hasBearing();

        mAccuracy = locationSampleEntity.getAccuracy();
        mHasAccuracy = locationSampleEntity.hasAccuracy();
    }

    /**
     * Construct a new Location Sample that has only time and location.
     */
    public LocationSampleData(PointGeometryData point, long time) {
        mPoint = point;
        mTime = time;

        mHasSpeed = false;
        mHasBearing = false;
        mHasAccuracy = false;
    }

    public void setProvider(String provider) {
        mProvider = provider;
    }

    public void setSpeed(float speed) {
        mSpeed = speed;
        mHasSpeed = true;
    }

    public void setBearing(float bearing) {
        mBearing = bearing;
        mHasBearing = true;
    }

    public void setAccuracy(float accuracy) {
        mAccuracy = accuracy;
        mHasAccuracy = true;
    }

    /**
     * Returns the name of the provider that generated this fix.
     *
     * @return the provider, or null if it has not been set
     */
    public String getProvider() {
        return mProvider;
    }

    public long getTime() {
        return mTime;
    }

    /**
     * Get the location.
     */
    public PointGeometryData getLocation() {
        return new PointGeometryData(mPoint);
    }

    /**
     * True if this location has a speed.
     */
    public boolean hasSpeed() {
        return mHasSpeed;
    }

    /**
     * Get the speed if it is available, in meters/second over ground.
     * <p/>
     * <p>If this location does not have a speed then 0.0 is returned.</p>
     */
    public float getSpeed() {
        return mSpeed;
    }

    /**
     * True if this location has a bearing.
     */
    public boolean hasBearing() {
        return mHasBearing;
    }

    /**
     * Get the bearing, in degrees.
     * <p/>
     * <p>Bearing is the horizontal direction of travel of this device,
     * and is not related to the device orientation. It is guaranteed to
     * be in the range (0.0, 360.0] if the device has a bearing.
     * </p>
     * <p>If this location does not have a bearing then 0.0 is returned.</p>
     */
    public float getBearing() {
        return mBearing;
    }

    /**
     * True if this location has an accuracy.
     */
    public boolean hasAccuracy() {
        return mHasAccuracy;
    }

    /**
     * Get the estimated accuracy of this location, in meters.
     * <p/>
     * <p>We define accuracy as the radius of 68% confidence. In other
     * words, if you draw a circle centered at this location's
     * latitude and longitude, and with a radius equal to the accuracy,
     * then there is a 68% probability that the true location is inside
     * the circle.
     * </p>
     * <p>In statistical terms, it is assumed that location errors
     * are random with a normal distribution, so the 68% confidence circle
     * represents one standard deviation. Note that in practice, location
     * errors do not always follow such a simple distribution.
     * </p>
     * <p>This accuracy estimation is only concerned with horizontal
     * accuracy, and does not indicate the accuracy of bearing,
     * velocity or altitude if those are included in this Location.
     * </p>
     */
    public float getAccuracy() {
        return mAccuracy;
    }

    public long getAgeMillis() {
        return System.currentTimeMillis() - mTime;
    }
}
