package com.teamagam.gimelgimel.domain.messages.entities.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;

import java.util.Date;

/**
 * Created on 5/18/2016.
 * MessageImage Content.
 */
public class ImageMetadata {

    public static final String USER = "User";
    public static final String SENSOR = "Sensor";

    @SerializedName("location")
    private PointGeometry mPoint;

    @SerializedName("timeStamp")
    private long mTime;

    @SerializedName("hasLocation")
    private boolean mHasLocation = false;

    @SerializedName("sourceType")
    private String mSource;

    @SerializedName("url")
    private String mURL;

    /**
     * Construct a new Image Metadata that has only time and source.
     * time and source are must
     */
    public ImageMetadata(long time, String source) {
        mTime = time;
        mSource = source;
    }

    /**
     * Construct a new Image Metadata that has only time, source and URL.
     * time and source are must
     */
    public ImageMetadata(long time, String url, String source) {
        mTime = time;
        mSource = source;
        mURL = url;
    }


    /**
     * Construct a new Image Metadata that has time, source and location W/O URL.
     */
    public ImageMetadata(long time, PointGeometry loc, String source) {
        mTime = time;
        mSource = source;
        mPoint = loc;
        mHasLocation = mPoint != null;
    }

    /**
     * Construct a new Image Metadata that has time, source, location and URL.
     */
    public ImageMetadata(long time, String url, PointGeometry loc, String source) {
        mTime = time;
        mSource = source;
        mPoint = loc;
        mHasLocation = true;
        mURL = url;
    }


    /**
     * Return the UTC time of this fix, in milliseconds since January 1, 1970.
     * <p/>
     *
     * @return time of fix, in milliseconds since January 1, 1970.
     */
    public long getTime() {
        return mTime;
    }

    /**
     * Get the location..
     * <p/>
     *
     * @return Location
     */
    public PointGeometry getLocation() {
        if (mPoint != null) {
            return new PointGeometry(mPoint);
        } else {
            return null;
        }
    }

    /**
     * Set the location..
     * <p/>
     *
     * @param point
     * @return Location
     */
    public void setLocation(PointGeometry point) {
        mPoint = point;
        mHasLocation = true;
    }


    /**
     * True if this has location.
     */
    public boolean hasLocation() {
        return mHasLocation;
    }

    public String getSource() {
        return mSource;
    }

    public void setURL(String url) {
        mURL = url;
    }

    /**
     * returns URL
     *
     * @return url, may be null.
     */
    public String getURL() {
        return mURL;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("ImageMetadata[");
        s.append(mSource);
        s.append(": ");
        if (mTime == 0) {
            s.append(" t=?!?");
        } else {
            s.append(" t=");
            s.append(new Date(mTime));
        }

        if (hasLocation()) {
            s.append(" ");
            s.append(mPoint);
        }

        if (mURL != null) {
            s.append(" url=");
            s.append(mURL);
        }
        s.append(']');
        return s.toString();
    }
}
