package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.alerts.entity.GeoAlert;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

import java.util.Date;

public class MessageGeoAlert extends MessageAlert implements GeoEntityHolder {

    private final AlertEntity mAlertEntity;

    public MessageGeoAlert(String messageId, String senderId, Date createdAt,
                           GeoAlert alert) {
        super(messageId, senderId, createdAt, alert);
        mAlertEntity = alert.getEntity();
    }

    @Override
    public GeoEntity getGeoEntity() {
        return mAlertEntity;
    }
}
