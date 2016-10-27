package com.teamagam.gimelgimel.app.message.model;

import android.support.annotation.StringDef;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;


/**
 * A class representing a type of ic_message passed to the server
 */
public abstract class MessageApp<T> implements IMessageAppVisitable {

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TEXT, GEO, USER_LOCATION, IMAGE})
    public @interface MessageType {}
    public static final String TEXT = "Text";
    public static final String GEO = "Geo";
    public static final String USER_LOCATION = "UserLocation";
    public static final String IMAGE = "Image";

    protected T mContent;

    private String mMessageId;

    private String mSenderId;
    private Date mCreatedAt;

    private
    @MessageType
    String mType;

    private boolean isSelected;

    private boolean isRead;

    public MessageApp(String senderId, @MessageType String type) {
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
