package com.gimelgimel.domain.model;

import com.gimelgimel.domain.model.visitor.IMessageVisitable;

import java.util.Date;

public abstract class MessageModel implements IMessageVisitable {

    private String mMessageId;
    private String mSenderId;
    private Date mCreatedAt;

    public MessageModel(String senderId) {
        this.mSenderId = senderId;
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
