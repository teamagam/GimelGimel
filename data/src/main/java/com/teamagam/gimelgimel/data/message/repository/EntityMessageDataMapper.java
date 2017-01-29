package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class EntityMessageDataMapper implements EntityMessageMapper {

    private Map<String, String> mEntityToMessage;
    private Map<String, String> mMessageToEntity;

    @Inject
    public EntityMessageDataMapper() {
        mEntityToMessage = new TreeMap<>();
        mMessageToEntity = new TreeMap<>();
    }

    @Override
    public void addMapping(String messageId, String entityId) {
        mEntityToMessage.put(entityId, messageId);
        mMessageToEntity.put(messageId, entityId);
    }

    @Override
    public Observable<String> getEntityId(Message message) {
        return Observable.just(message)
                .map(m -> mMessageToEntity.get(m.getMessageId()));
    }

    @Override
    public Observable<String> getEntityId(String messageId) {
        return Observable.just(messageId)
                .map(id -> mMessageToEntity.get(id));
    }

    @Override
    public Observable<String> getMessageId(GeoEntity entity) {
        return Observable.just(entity)
                .map(e -> mEntityToMessage.get(e.getId()));
    }

    @Override
    public Observable<String> getMessageId(String entityId) {
        return Observable.just(entityId)
                .map(id -> mEntityToMessage.get(id));
    }
}
