package com.teamagam.gimelgimel.domain.messages.entities.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;

import java.util.Date;

/**
 * Created on 4/18/2016.
 * <p/>
 * An immutable data class representing a geographic location sample.
 * <p/>
 * <p>A location can consist of a {@link PointGeometry} , timestamp,
 * and other information such as bearing, altitude and velocity.
 * <p/>
 * <p>All locations  are
 * guaranteed to have a valid latitude, longitude, and timestamp
 * (UTC time), all other
 * parameters are optional.
 */
public class LocationSample {

    @SerializedName("location")
    private PointGeometry mPoint;

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

    /**
     * Construct a new Location Sample that has only time and location.
     */
    public LocationSample(PointGeometry point, long time) {
        mPoint = point;
        mTime = time;
    }

    /**
     * Returns the name of the provider that generated this fix.
     *
     * @return the provider, or null if it has not been set
     */
    public String getProvider() {
        return mProvider;
    }

    /**
     * Return the UTC time of this fix, in milliseconds since January 1, 1970.
     * <p/>
     * <p>On the other hand, {@link #getTime} is useful for presenting
     * a human readable time to the user, or for carefully comparing
     * location fixes across reboot or across devices.
     * <p/>
     * <p>All locations
     * are guaranteed to have a valid UTC time, however remember that
     * the system time may have changed since the location was generated.
     *
     * @return time of fix, in milliseconds since January 1, 1970.
     */
    public long getTime() {
        return mTime;
    }

    /**
     * Get the location..
     * <p/>
     * <p>All locations
     * will have a valid location.
     */
    public PointGeometry getLocation() {
        return new PointGeometry(mPoint);
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
     * <p>If this location does not have a speed then 0.0 is returned.
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
     * <p/>
     * <p>If this location does not have a bearing then 0.0 is returned.
     */
    public float getBearing() {
        return mBearing;
    }

    /**
     * True if this location has an accuracy.
     * <p/>
     * <p>All locations have an
     * accuracy.
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
     * <p/>
     * <p>In statistical terms, it is assumed that location errors
     * are random with a normal distribution, so the 68% confidence circle
     * represents one standard deviation. Note that in practice, location
     * errors do not always follow such a simple distribution.
     * <p/>
     * <p>This accuracy estimation is only concerned with horizontal
     * accuracy, and does not indicate the accuracy of bearing,
     * velocity or altitude if those are included in this Location.
     * <p/>
     * <p>If this location does not have an accuracy, then 0.0 is returned.
     * All locations include
     * an accuracy.
     */
    public float getAccuracy() {
        return mAccuracy;
    }

    public long getAgeMillis() {
        return System.currentTimeMillis() - mTime;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Location[");
        s.append(mProvider);
        s.append(" ");
        s.append(mPoint);
        if (mHasAccuracy) {
            s.append(String.format(" acc=%.0f", mAccuracy));
        } else {
            s.append(" acc=???");
        }
        if (mTime == 0) {
            s.append(" t=?!?");
        } else {
            s.append(" t=");
            s.append(new Date(mTime));
        }

        if (mPoint.hasAltitude) {
            s.append(" alt=").append(mPoint.altitude);
        }
        if (mHasSpeed) {
            s.append(" vel=").append(mSpeed);
        }
        if (mHasBearing) {
            s.append(" bear=").append(mBearing);
        }

        s.append(']');
        return s.toString();
    }
}
