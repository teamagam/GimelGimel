package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    public String getEntityId(Message message) {
        return mMessageToEntity.get(message.getMessageId());
    }

    @Override
    public String getEntityId(String messageId) {
        return mMessageToEntity.get(messageId);
    }

    @Override
    public String getMessageId(GeoEntity entity) {
        return mEntityToMessage.get(entity.getId());
    }

    @Override
    public String getMessageId(String entityId) {
        return mEntityToMessage.get(entityId);
    }
}
