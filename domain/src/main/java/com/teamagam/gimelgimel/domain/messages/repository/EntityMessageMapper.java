package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import rx.Observable;

public interface EntityMessageMapper {
    void addMapping(String messageId, String entityId);

    String getEntityId(Message message);

    String getEntityId(String messageId);

    String getMessageId(GeoEntity entity);

    String getMessageId(String entityId);
}
