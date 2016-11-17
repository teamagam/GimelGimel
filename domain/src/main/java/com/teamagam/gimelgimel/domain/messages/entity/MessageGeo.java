package com.teamagam.gimelgimel.domain.messages.entity;


import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

import java.util.Date;

public class MessageGeo extends BaseMessageGeo {

    private GeoEntity mGeoEntity;

    public MessageGeo(String messageId, String senderId, Date createdAt, GeoEntity geoEntity) {
        super(messageId, senderId, createdAt);
        mGeoEntity = geoEntity;
    }

    @Override
    public void accept(IMessageVisitor visitor) {
        visitor.visit(this);
    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
    }

    @Override
    public GeoEntity extractGeoEntity() {
        return getGeoEntity();
    }
}


