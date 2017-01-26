package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;

import java.util.HashMap;


public class EntityMessageDataMapper implements EntityMessageMapper {

    private HashMap<String, Message> mGeometryToMessage;
    private HashMap<String, GeoEntity> mMessageToGeometry;

    public EntityMessageDataMapper() {
        mGeometryToMessage = new HashMap<>();
        mMessageToGeometry = new HashMap<>();
    }

    @Override
    public void addMapping(Message message, GeoEntity entity) {
        mGeometryToMessage.put(entity.getId(), message);
        mMessageToGeometry.put(message.getMessageId(), entity);
    }

    @Override
    public GeoEntity getEntity(Message message) {
        return mMessageToGeometry.get(message.getMessageId());
    }

    @Override
    public GeoEntity getEntity(String messageId) {
        return mMessageToGeometry.get(messageId);
    }

    @Override
    public Message getMessage(GeoEntity entity) {
        return mGeometryToMessage.get(entity.getId());
    }

    @Override
    public Message getMessage(String entityId) {
        return mGeometryToMessage.get(entityId);
    }
}
