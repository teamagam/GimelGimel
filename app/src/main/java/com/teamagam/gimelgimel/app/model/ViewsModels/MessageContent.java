package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.annotations.SerializedName;

/**
 * Data-Type class for {@link Message}'s inner content
 */
public class MessageContent {

    @SerializedName("text")
    private String mText = null;

    /**
     * We want to avoid deserialize of long/lat fields in-case they're not initialized.
     * Float is used since GSON only deserialize initialized fields into JSON.
     * float is always initialized with 0, while Float is initialized with null.
     */
    @SerializedName("longitude")
    private Float mLongitude;

    @SerializedName("latitude")
    private Float mLatitude;

    public MessageContent(String text) {
        this.mText = text;
    }

    public MessageContent(Float longitude, Float latitude) {
        this.mLongitude = longitude;
        this.mLatitude = latitude;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public Float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Float mLongitude) {
        this.mLongitude = mLongitude;
    }

    public Float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Float mLatitude) {
        this.mLatitude = mLatitude;
    }
}
