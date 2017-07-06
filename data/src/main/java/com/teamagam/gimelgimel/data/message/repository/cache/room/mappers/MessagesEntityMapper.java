package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.AlertFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ImageFeatureEntity;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessagesEntityMapper implements EntityMapper<ChatMessage, ChatMessageEntity> {

  private GeoEntityDataMapper mGeoEntityDataMapper;
  private ChatMessageFeaturesToEntityFeatures mFeaturesToEntityFeatures;

  @Inject
  public MessagesEntityMapper(GeoEntityDataMapper geoEntityDataMapper,
      ChatMessageFeaturesToEntityFeatures featuresToEntityFeatures) {
    mGeoEntityDataMapper = geoEntityDataMapper;
    mFeaturesToEntityFeatures = featuresToEntityFeatures;
  }

  public ChatMessage mapToDomain(ChatMessageEntity messageEntity) {
    if (messageEntity == null) {
      return null;
    }

    return new ChatMessage(messageEntity.messageId, messageEntity.senderId,
        messageEntity.creationDate, createFeaturesFromEntity(messageEntity));
  }

  public ChatMessageEntity mapToEntity(ChatMessage message) {
    if (message == null) {
      return null;
    }

    return createChatMessageEntity(message);
  }

  private MessageFeatureVisitable[] createFeaturesFromEntity(ChatMessageEntity entity) {
    List<MessageFeatureVisitable> features = new ArrayList<>();

    for (ChatMessageEntity.Feature feature : entity.features) {
      features.add(createFeature(entity, feature));
    }

    return features.toArray(new MessageFeatureVisitable[features.size()]);
  }

  private MessageFeatureVisitable createFeature(ChatMessageEntity entity,
      ChatMessageEntity.Feature feature) {
    switch (feature) {
      case TEXT:
        return new TextFeature(entity.text);
      case GEO:
        return new GeoFeature(convertGeoEntityToDomain(entity.geoFeatureEntity));
      case IMAGE:
        return convertImageFeatureEntityToDomain(entity.imageFeatureEntity);
      case ALERT:
        return new AlertFeature(convertAlertEntityToDomain(entity.alertFeatureEntity));
      default:
        throw new RuntimeException("Unknown feature found in db entity: " + feature);
    }
  }

  private GeoEntity convertGeoEntityToDomain(GeoFeatureEntity geoFeatureEntity) {
    return mGeoEntityDataMapper.transform(geoFeatureEntity.id,
        new GeoContentData(geoFeatureEntity.geometry, geoFeatureEntity.text));
  }

  private ImageFeature convertImageFeatureEntityToDomain(ImageFeatureEntity imageFeatureEntity) {
    return new ImageFeature(imageFeatureEntity.time, imageFeatureEntity.source,
        imageFeatureEntity.remoteUrl, imageFeatureEntity.localUrl);
  }

  private Alert convertAlertEntityToDomain(AlertFeatureEntity alertEntity) {
    return new Alert(alertEntity.alertId, alertEntity.severity, alertEntity.text,
        alertEntity.source, alertEntity.time, getAlertType(alertEntity.type));
  }

  private Alert.Type getAlertType(int type) {
    return Alert.Type.values()[type];
  }

  private ChatMessageEntity createChatMessageEntity(ChatMessage message) {
    ChatMessageEntity entity = new ChatMessageEntity();

    entity.messageId = message.getMessageId();
    entity.senderId = message.getSenderId();
    entity.creationDate = message.getCreatedAt();

    entity = mFeaturesToEntityFeatures.addFeaturesToEntity(entity, message);
    return entity;
  }
}
