package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class MessageAlert extends BaseMessageGeo {

    private final Alert mAlert;

    public MessageAlert(String messageId, String senderId, Date createdAt, Alert alert) {
        super(messageId, senderId, createdAt);
        mAlert = alert;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public Alert getAlert() {
        return mAlert;
    }

    @Override
    public GeoEntity extractGeoEntity() {
        return mAlert.getEntity();
    }
}
