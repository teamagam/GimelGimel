package com.teamagam.gimelgimel.data.message.entity;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * A class representing a type of ic_message passed to the server
 */
public abstract class MessageData<T> implements IMessageDataVisitable {

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public void setSenderId(String senderId) {
        this.mSenderId = senderId;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MessageType {}

    public static final String TEXT = "Text";
    public static final String GEO = "Geo";
    public static final String USER_LOCATION = "UserLocation";
    public static final String IMAGE = "Image";
    public static final String SENSOR = "Sensor";
    public static final String ALERT = "Alert";
    public static final String VECTOR_LAYER = "VectorLayer";

    @SerializedName("content")
    protected T mContent;

    @SerializedName("_id")
    private String mMessageId;

    @SerializedName("senderId")
    private String mSenderId;

    @SerializedName("createdAt")
    private Date mCreatedAt;

    @SerializedName("type")
    private @MessageType String mType;

    public MessageData(@MessageType String type) {
        this.mType = type;
    }

    public void setContent(T content) {
        mContent = content;
    }

    public T getContent() {
        return mContent;
    }

    public String getMessageId() {
        return mMessageId;
    }

    public void setMessageId(String messageId) {
        mMessageId = messageId;
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
}
