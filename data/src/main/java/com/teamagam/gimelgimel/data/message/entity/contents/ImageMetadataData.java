package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.geometry.entity.PointGeometryData;
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
    private String mURL;

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
    public ImageMetadataData(long time, String url, @SourceType String source) {
        mTime = time;
        mSource = source;
        mURL = url;
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
    public ImageMetadataData(long time, String url, PointGeometryData loc, @SourceType String source) {
        mTime = time;
        mSource = source;
        mPoint = loc;
        mHasLocation = true;
        mURL = url;
    }

    public ImageMetadataData(ImageMetadata metadata, PointGeometryData loc){
        mTime = metadata.getTime();
        mSource = metadata.getSource();
        mPoint = loc;
        mHasLocation = metadata.hasLocation();
        mURL = metadata.getURL();
    }

    public ImageMetadataData(ImageMetadata metadata){
        mTime = metadata.getTime();
        mSource = metadata.getSource();
        mURL = metadata.getURL();
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
}
