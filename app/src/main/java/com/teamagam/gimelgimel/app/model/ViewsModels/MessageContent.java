package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */
public class MessageContent {

    @SerializedName("text")
    String mText = null;

    @SerializedName("longitude")
    float mLongitude;

    @SerializedName("latitude")
    float mLatitude;


    public MessageContent(String text)
    {
        this.mText = text;
    }

    public MessageContent(float longitute, float latitude)
    {
        this.mLatitude = longitute;
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
