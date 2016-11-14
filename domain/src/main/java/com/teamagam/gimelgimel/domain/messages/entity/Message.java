package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitable;

import java.util.Date;

public abstract class Message implements IMessageVisitable {

    private String mMessageId;
    private String mSenderId;
    private Date mCreatedAt;
    private boolean mIsSelected;
    private boolean mIsRead;

    public Message(String messageId, String senderId, Date createdAt, boolean isRead, boolean isSelected) {
        mMessageId = messageId;
        mSenderId = senderId;
        mCreatedAt = createdAt;
        mIsRead = isRead;
        mIsSelected = isSelected;
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

    @Override
    public boolean equals(Object o) {
        if(o instanceof Message){
            return mMessageId.equals(((Message) o).getMessageId());
        } else {
            return super.equals(o);
        }
    }
}
