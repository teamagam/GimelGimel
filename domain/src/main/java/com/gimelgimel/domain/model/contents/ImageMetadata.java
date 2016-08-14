package com.gimelgimel.domain.model.contents;

import com.gimelgimel.domain.model.PointGeometry;

import java.util.Date;

public class ImageMetadata {

    private PointGeometry mPoint;
    private boolean mHasLocation;
    private long mTime;
    private String mSource;
    private String mURL;

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
