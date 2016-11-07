package com.teamagam.gimelgimel.domain.messages.entity;


import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class MessageGeo extends Message {

    private GeoEntity mGeoEntity;
    private String mText;

    public MessageGeo(String messageId, String senderId, Date createdAt, boolean isRead,
                      boolean isSelected, GeoEntity geoEntity,
                      String text) {
        super(messageId, senderId, createdAt, isRead, isSelected);
        mGeoEntity = geoEntity;
        mText = text;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
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
        s.append("entity= ").append(mGeoEntity);
        s.append(']');
        return s.toString();
    }
}


