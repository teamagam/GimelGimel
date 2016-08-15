package com.teamagam.gimelgimel.domain.messages.entities;

import com.teamagam.gimelgimel.domain.messages.entities.interfaces.visitor.IMessageVisitable;

import java.util.Date;


/**
 * A class representing a type of ic_message passed to the server
 */
public abstract class Message<T> implements IMessageVisitable {

    public static final String TEXT = "Text";
    public static final String GEO = "Geo";
    public static final String USER_LOCATION = "UserLocation";
    public static final String IMAGE = "Image";

    protected T mContent;

    private String mMessageId;

    private String mSenderId;

    private Date mCreatedAt;

    private String mType;

    public Message(String senderId, String type) {
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

    public String getType() {
        return mType;
    }

}
