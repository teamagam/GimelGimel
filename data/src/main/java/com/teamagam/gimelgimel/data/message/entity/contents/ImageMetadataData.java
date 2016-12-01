package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.map.entity.PointGeometryData;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ImageMetadataData {

    @Retention(RetentionPolicy.SOURCE)
    public @interface SourceType {
    }

    public static final String USER = "User";
    public static final String SENSOR = "Sensor";

    @SerializedName("location")
    private PointGeometryData mPoint;

    @SerializedName("timeStamp")
    private long mTime;

    @SerializedName("hasLocation")
    private boolean mHasLocation = false;

    @SourceType
    @SerializedName("sourceType")
    private String mSource;

    @SerializedName("url")
    private String mRemoteUrl;

    private transient String mLocalUrl;

    /**
     * Construct a new Image Metadata that has only time and source.
     * time and source are must
     */
    public ImageMetadataData(long time, @SourceType String source) {
        mTime = time;
        mSource = source;
    }

    /**
     * Construct a new Image Metadata that has only time, source and URL.
     * time and source are must
     */
    public ImageMetadataData(long time, String remoteUrl, String localUrl,
                             @SourceType String source) {
        mTime = time;
        mSource = source;
        mRemoteUrl = remoteUrl;
        mLocalUrl = localUrl;
    }


    /**
     * Construct a new Image Metadata that has time, source and location W/O URL.
     */
    public ImageMetadataData(long time, PointGeometryData loc, @SourceType String source) {
        mTime = time;
        mSource = source;
        mPoint = loc;
        mHasLocation = mPoint != null;
    }

    /**
     * Construct a new Image Metadata that has time, source, location and URL.
     */
    public ImageMetadataData(long time, String remoteUrl, String localUrl, PointGeometryData loc,
                             @SourceType String source) {
        mTime = time;
        mSource = source;
        mPoint = loc;
        mHasLocation = true;
        mRemoteUrl = remoteUrl;
        mLocalUrl = localUrl;
    }

    public ImageMetadataData(ImageMetadata metadata, PointGeometryData loc) {
        mTime = metadata.getTime();
        mSource = metadata.getSource();
        mPoint = loc;
        mHasLocation = metadata.hasLocation();
        mRemoteUrl = metadata.getRemoteUrl();
    }

    public ImageMetadataData(ImageMetadata metadata) {
        mTime = metadata.getTime();
        mSource = metadata.getSource();
        mRemoteUrl = metadata.getRemoteUrl();
        mHasLocation = false;
    }


    /**
     * Return the UTC time of this fix, in milliseconds since January 1, 1970.
     * <p>
     *
     * @return time of fix, in milliseconds since January 1, 1970.
     */
    public long getTime() {
        return mTime;
    }

    /**
     * Get the location..
     * <p>
     *
     * @return Location
     */
    public PointGeometryData getLocation() {
        if (mPoint != null) {
            return new PointGeometryData(mPoint);
        } else {
            return null;
        }
    }

    /**
     * Set the location..
     * <p>
     *
     * @param point
     * @return Location
     */
    public void setLocation(PointGeometryData point) {
        mPoint = point;
        mHasLocation = true;
    }


    /**
     * True if this has location.
     */
    public boolean hasLocation() {
        return mHasLocation;
    }

    @SourceType
    public String getSource() {
        return mSource;
    }

    public void setURL(String url) {
        mRemoteUrl = url;
    }

    /**
     * returns remote URL
     *
     * @return url, may be null.
     */
    public String getRemoteUrl() {
        return mRemoteUrl;
    }

    public String getLocalUrl() {
        return mLocalUrl;
    }
}
