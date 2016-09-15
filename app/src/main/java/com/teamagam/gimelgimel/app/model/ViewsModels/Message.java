package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;


/**
 * A class representing a type of ic_message passed to the server
 */
public abstract class Message<T> implements IMessageVisitable {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TEXT, GEO, USER_LOCATION, IMAGE})
    public @interface MessageType {}
    public static final String TEXT = "Text";
    public static final String GEO = "Geo";
    public static final String USER_LOCATION = "UserLocation";
    public static final String IMAGE = "Image";
    @SerializedName("content")
    protected T mContent;
    @SerializedName("_id")
    private String mMessageId;
    @SerializedName("senderId")
    private String mSenderId;
    @SerializedName("createdAt")
    private Date mCreatedAt;

    @SerializedName("type")
    private
    @MessageType
    String mType;

    public Message(String senderId, @MessageType String type) {
        this.mSenderId = senderId;
        this.mType = type;
    }

    public T getContent() {
        return mContent;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public @MessageType String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public void setSenderId(String mSenderId) {
        this.mSenderId = mSenderId;
    }

    public void setMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }

    public void setContent(T mContent) {
        this.mContent = mContent;
    }

}
