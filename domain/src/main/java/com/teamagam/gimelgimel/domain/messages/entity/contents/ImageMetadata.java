package com.teamagam.gimelgimel.domain.messages.entity.contents;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

import java.util.Date;

public class ImageMetadata {

    private boolean mHasLocation;
    private long mTime;
    private String mSource;
    private String mURL;
    private GeoEntity mGeoEntity;

    /**
     * Construct a new Image Metadata that has time, source, entityId and URL.
     */
    public ImageMetadata(long time, String url, GeoEntity geoEntity, String source) {
        mTime = time;
        mSource = source;
        mURL = url;
        mGeoEntity = geoEntity;

        if (geoEntity != null) {
            mHasLocation = true;
        }
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

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
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
            s.append(" GeoEntity:");
            s.append(mGeoEntity);
        }

        if (mURL != null) {
            s.append(" url=");
            s.append(mURL);
        }
        s.append(']');
        return s.toString();
    }
}
