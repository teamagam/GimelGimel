package com.teamagam.gimelgimel.app.model.ViewsModels;

import java.util.Date;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */

/**
 * A class representing a type of ic_message passed to the server
 */
public class Message {

    public String mMessageId;
    public String mSenderId;
    public Date mCreatedAt;
    public String mType;
    public MessageContent mContent;

    public Message(String senderId, String type, MessageContent content)
    {
        this.mSenderId = senderId;
        this.mType = type;
        this.mContent = content;
    }

    public String getmMessageId() {
        return mMessageId;
    }

    public String getmSenderId() {
        return mSenderId;
    }

    public Date getmCreatedAt() {
        return mCreatedAt;
    }

    public String getmType() {
        return mType;
    }

    public MessageContent getmContent() {
        return mContent;
    }

    public void setmMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }

    public void setmSenderId(String mSenderId) {
        this.mSenderId = mSenderId;
    }

    public void setmCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public void setmContent(MessageContent mContent) {
        this.mContent = mContent;
    }
}
