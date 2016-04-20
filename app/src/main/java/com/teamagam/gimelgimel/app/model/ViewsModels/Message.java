package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


/**
 * A class representing a type of ic_message passed to the server
 */
public abstract class Message<T>{

    @StringDef({TEXT, LAT_LONG, USER_LOCATION})
    public @interface MessageType{}
    public static final String TEXT = "Text";
    public static final String LAT_LONG = "LatLong";
    public static final String USER_LOCATION = "UserLocation";

    @SerializedName("_id")
    private String mMessageId;
    @SerializedName("senderId")
    private String mSenderId;
    @SerializedName("createdAt")
    private Date mCreatedAt;
    @SerializedName("type")
    private @MessageType String mType;
    @SerializedName("content")
    protected T mContent;

    public Message(String senderId, @MessageType String type) {
        this.mSenderId = senderId;
        this.mType = type;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String mSenderId) {
        this.mSenderId = mSenderId;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

}
