package com.teamagam.gimelgimel.domain.messages.entity;


import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class MessageGeo extends Message {

    private String mEntityId;

    public MessageGeo(String messageId, String senderId, Date createdAt, String entityId) {
        super(messageId, senderId, createdAt);
        mEntityId = entityId;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public String getEntityId() {
        return mEntityId;
    }

}


