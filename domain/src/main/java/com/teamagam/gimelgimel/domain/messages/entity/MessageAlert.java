package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.alerts.entity.GeoAlert;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class MessageAlert extends Message implements GeoEntityHolder {

    private final GeoAlert mAlert;

    public MessageAlert(String messageId, String senderId, Date createdAt, GeoAlert alert) {
        super(messageId, senderId, createdAt);
        mAlert = alert;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public GeoAlert getAlert() {
        return mAlert;
    }

    @Override
    public GeoEntity getGeoEntity() {
        return mAlert.getEntity();
    }
}
