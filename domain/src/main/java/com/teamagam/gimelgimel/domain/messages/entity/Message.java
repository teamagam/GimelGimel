package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitable;

import java.util.Date;

public abstract class Message implements IMessageVisitable {

    private String mMessageId;
    private String mSenderId;
    private Date mCreatedAt;

    public Message(String senderId) {
        this.mSenderId = senderId;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
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
}
