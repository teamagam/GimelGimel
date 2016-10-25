package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitable;

import java.util.Date;

public abstract class Message implements IMessageVisitable {

    private String mMessageId;
    private String mSenderId;
    private Date mCreatedAt;
    private boolean mIsSelected;
    private boolean mIsRead;

    public Message(String senderId) {
        this.mSenderId = senderId;
    }

    public Message(String messageId, String senderId) {
        this.mMessageId = messageId;
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

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setRead(boolean isRead) {
        mIsRead = isRead;
    }

    public boolean isRead() {
        return mIsRead;
    }

}
