package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */

/**
 * A class representing a type of ic_message passed to the server
 */
public class Message {

    @SerializedName("_id")
    private String mMessageId;

    @SerializedName("senderId")
    private String mSenderId;

    @SerializedName("createdAt")
    private Date mCreatedAt;

    @SerializedName("type")
    private String mType;

    @SerializedName("content")
    private MessageContent mContent;

    public static final String TEXT = "Text";
    public static final String LAT_LONG = "LatLong";

    public Message(String senderId, MessageContent content, String type) {
        this.mSenderId = senderId;
        this.mType = type;
        this.mContent = content;
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

    public String getType() {
        return mType;
    }

    public MessageContent getContent() {
        return mContent;
    }

    public void setMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }

    public void setSenderId(String mSenderId) {
        this.mSenderId = mSenderId;
    }

    public void setCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public void setContent(MessageContent mContent) {
        this.mContent = mContent;
    }
}
