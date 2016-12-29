package com.teamagam.gimelgimel.app.message.model;

import android.support.annotation.StringDef;

import com.teamagam.gimelgimel.app.common.base.adapters.IdentifiedData;
import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;


/**
 * A class representing a type of ic_message passed to the server
 */
public abstract class MessageApp<T> implements IMessageAppVisitable, IdentifiedData {

    public static final String TEXT = "Text";
    public static final String GEO = "Geo";
    public static final String USER_LOCATION = "UserLocation";
    public static final String IMAGE = "Image";
    public static final String SENSOR = "Sensor";
    protected T mContent;
    private String mMessageId;
    private String mSenderId;
    private Date mCreatedAt;
    private
    @MessageType
    String mType;
    private boolean mIsSelected;
    private boolean mIsRead;
    private boolean mIsFromSelf;

    public MessageApp(@MessageType String type) {
        mType = type;
    }

    public boolean isRead() {
        return mIsRead;
    }

    public void setRead(boolean read) {
        mIsRead = read;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public T getContent() {
        return mContent;
    }

    public void setContent(T mContent) {
        this.mContent = mContent;
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

    @Override
    public String getId() {
        return mMessageId;
    }

    public
    @MessageType
    String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public boolean isFromSelf() {
        return mIsFromSelf;
    }

    public void setFromSelf(boolean fromSelf) {
        mIsFromSelf = fromSelf;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TEXT, GEO, USER_LOCATION, IMAGE, SENSOR})
    public @interface MessageType {
    }
}
