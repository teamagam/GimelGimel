package com.teamagam.gimelgimel.app.message.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;

import com.teamagam.gimelgimel.app.message.model.visitor.IMessageAppVisitable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;


/**
 * A class representing a type of ic_message passed to the server
 */
public abstract class MessageApp<T> implements IMessageAppVisitable, Parcelable {

    @SuppressWarnings("WrongConstant")
    protected MessageApp(Parcel in) {
        mMessageId = in.readString();
        mSenderId = in.readString();
        mType = in.readString();
        isSelected = in.readByte() != 0;
        isRead = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMessageId);
        dest.writeString(mSenderId);
        dest.writeString(mType);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeByte((byte) (isRead ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public MessageApp(@MessageType String type) {
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
