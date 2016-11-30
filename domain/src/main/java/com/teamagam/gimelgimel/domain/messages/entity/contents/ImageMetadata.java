package com.teamagam.gimelgimel.domain.messages.entity.contents;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

import java.util.Date;

public class ImageMetadata {

    private boolean mHasLocation;
    private long mTime;
    private String mSource;
    private GeoEntity mGeoEntity;
    private String mRemoteUrl;
    private String mLocalUrl;

    /**
     * Construct a new Image Metadata that has time, source, entityId and URL.
     */
    public ImageMetadata(long time, String remoteUrl, String localUrl, GeoEntity geoEntity, String source) {
        mTime = time;
        mSource = source;
        mRemoteUrl = remoteUrl;
        mLocalUrl = localUrl;
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

    public void setLocalUrl(String localUrl) {
        mLocalUrl = localUrl;
    }

    public String getLocalUrl() {
        return mLocalUrl;
    }

    /**
     * returns URL
     *
     * @return url, may be null.
     */
    public String getRemoteUrl() {
        return mRemoteUrl;
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

        if (mRemoteUrl != null) {
            s.append(" url=");
            s.append(mRemoteUrl);
        }
        s.append(']');
        return s.toString();
    }


}
