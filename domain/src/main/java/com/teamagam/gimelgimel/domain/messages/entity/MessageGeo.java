package com.teamagam.gimelgimel.domain.messages.entity;


import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class MessageGeo extends Message {

    private String mEntityId;
    private String mText;

    public MessageGeo(String messageId, String senderId, Date createdAt, boolean isRead,
                      boolean isSelected, String entityId,
                      String text) {
        super(messageId, senderId, createdAt, isRead, isSelected);
        mEntityId = entityId;
        mText = text;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public String getEntityId() {
        return mEntityId;
    }

    public String getText() {
        return mText;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        if (!mText.isEmpty()) {
            s.append("text=").append(mText);
        } else {
            s.append("text=?");
        }
        s.append("entityId= ").append(mEntityId);
        s.append(']');
        return s.toString();
    }
}


