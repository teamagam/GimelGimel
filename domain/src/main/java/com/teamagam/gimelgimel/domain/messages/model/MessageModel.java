package com.teamagam.gimelgimel.domain.messages.model;

import java.util.Date;

public abstract class MessageModel implements com.teamagam.gimelgimel.domain.messages.model.visitor.IMessageVisitable {

    private String mMessageId;
    private String mSenderId;
    private Date mCreatedAt;

    public MessageModel(String senderId) {
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
