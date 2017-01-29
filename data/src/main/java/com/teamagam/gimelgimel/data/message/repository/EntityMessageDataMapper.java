package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class EntityMessageDataMapper implements EntityMessageMapper {

    private HashMap<String, String> mGeometryToMessage;
    private HashMap<String, String> mMessageToGeometry;

    @Inject
    public EntityMessageDataMapper() {
        mGeometryToMessage = new HashMap<>();
        mMessageToGeometry = new HashMap<>();
    }

    @Override
    public void addMapping(String messageId, String entityId) {
        mGeometryToMessage.put(entityId, messageId);
        mMessageToGeometry.put(messageId, entityId);
    }

    @Override
    public Observable<String> getEntityId(Message message) {
        return Observable.just(message)
                .map(m -> mMessageToGeometry.get(m.getMessageId()));
    }

    @Override
    public Observable<String> getEntityId(String messageId) {
        return Observable.just(messageId)
                .map(id -> mMessageToGeometry.get(id));
    }

    @Override
    public Observable<String> getMessageId(GeoEntity entity) {
        return Observable.just(entity)
                .map(e -> mGeometryToMessage.get(e.getId()));
    }

    @Override
    public Observable<String> getMessageId(String entityId) {
        return Observable.just(entityId)
                .map(id -> mGeometryToMessage.get(id));
    }
}
