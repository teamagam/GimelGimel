package com.teamagam.gimelgimel.app.message.model.contents;

import android.support.annotation.StringDef;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * Created on 5/18/2016.
 * MessageImageData Content.
 */
public class ImageMetadataApp {


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({USER, SENSOR})
    public @interface SourceType {
    }

    public static final String USER = "User";
    public static final String SENSOR = "Sensor";

    private PointGeometryApp mPoint;

    private long mTime;

    private boolean mHasLocation = false;

//    @SourceType
    private String mSource;

    private String mURL;

    /**
     * Construct a new Image Metadata that has only time and source.
     * time and source are must
     */
    public ImageMetadataApp(long time, /*@SourceType*/ String source) {
        mTime = time;
        mSource = source;
    }

    /**
     * Construct a new Image Metadata that has only time, source and URL.
     * time and source are must
     */
    public ImageMetadataApp(long time, String url, /*@SourceType*/ String source) {
        mTime = time;
        mSource = source;
        mURL = url;
    }


    /**
     * Construct a new Image Metadata that has time, source and location W/O URL.
     */
    public ImageMetadataApp(long time, PointGeometryApp loc, /*@SourceType*/ String source) {
        mTime = time;
        mSource = source;
        mPoint = loc;
        mHasLocation = mPoint != null;
    }

    /**
     * Construct a new Image Metadata that has time, source, location and URL.
     */
    public ImageMetadataApp(long time, String url, PointGeometryApp loc, /*@SourceType*/ String source) {
        this(time, loc, source);
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
    public PointGeometryApp getLocation() {
        if (mPoint != null) {
            return new PointGeometryApp(mPoint);
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
    public void setLocation(PointGeometryApp point) {
        mPoint = point;
        mHasLocation = true;
    }


    /**
     * True if this has location.
     */
    public boolean hasLocation() {
        return mHasLocation;
    }

//    @SourceType
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
        s.append("ImageMetadataData[");
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
