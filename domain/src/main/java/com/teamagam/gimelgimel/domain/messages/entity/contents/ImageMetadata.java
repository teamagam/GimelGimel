package com.teamagam.gimelgimel.domain.messages.entity.contents;

import java.util.Date;

public class ImageMetadata {

    private String mEntityId;
    private boolean mHasLocation;
    private long mTime;
    private String mSource;
    private String mURL;

    /**
     * Construct a new Image Metadata that has time, source, entityId and URL.
     */
    public ImageMetadata(long time, String url, String entityId, String source) {
        mTime = time;
        mSource = source;
        mURL = url;
        mEntityId = entityId;

        if (entityId != null) {
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

    /**
     * Get the entityID
     * <p/>
     *
     * @return String
     */
    public String getEntityId() {
        return mEntityId;
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
            s.append(" entityId:");
            s.append(mEntityId);
        }

        if (mURL != null) {
            s.append(" url=");
            s.append(mURL);
        }
        s.append(']');
        return s.toString();
    }
}
