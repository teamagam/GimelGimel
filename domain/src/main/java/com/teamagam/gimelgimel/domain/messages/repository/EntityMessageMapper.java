package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

public interface EntityMessageMapper {
    void addMapping(Message message, GeoEntity entity);

    GeoEntity getEntity(Message message);

    GeoEntity getEntity(String messageId);

    Message getMessage(GeoEntity entity);

    Message getMessage(String entityId);
}
