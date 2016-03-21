package com.teamagam.gimelgimel.app.model.ViewsModels;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */
public class MessageContent {

    String mText = null;
    Float mLongitute = 0.0f;
    Float mLatitude =0.0f ;


    public MessageContent(String text)
    {
        this.mText = text;
    }

    public MessageContent(float longitute, float latitude)
    {
        this.mLatitude = longitute;
        this.mLatitude = latitude;
    }
    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public Float getmLongitute() {
        return mLongitute;
    }

    public void setmLongitute(Float mLongitute) {
        this.mLongitute = mLongitute;
    }

    public Float getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(Float mLatitude) {
        this.mLatitude = mLatitude;
    }
}
