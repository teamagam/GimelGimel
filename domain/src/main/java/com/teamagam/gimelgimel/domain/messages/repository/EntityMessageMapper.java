package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import rx.Observable;

public interface EntityMessageMapper {
    void addMapping(String messageId, String entityId);

    Observable<String> getEntityId(Message message);

    Observable<String> getEntityId(String messageId);

    Observable<String> getMessageId(GeoEntity entity);

    Observable<String> getMessageId(String entityId);
}
