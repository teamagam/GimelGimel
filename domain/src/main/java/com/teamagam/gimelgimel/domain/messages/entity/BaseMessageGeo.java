package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

import java.util.Date;

public abstract class BaseMessageGeo extends Message {

    public BaseMessageGeo(String messageId, String senderId, Date createdAt) {
        super(messageId, senderId, createdAt);
    }

    public abstract GeoEntity getGeoEntity();
}