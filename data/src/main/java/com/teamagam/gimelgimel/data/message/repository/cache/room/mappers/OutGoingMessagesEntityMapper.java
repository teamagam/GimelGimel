package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.AlertFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ImageFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.IconData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.Style;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
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
public class OutGoingMessagesEntityMapper
    implements EntityMapper<OutGoingChatMessage, OutGoingChatMessageEntity> {

  private GeoFeatureEntityMapper mGeoFeatureEntityMapper;
  private OutGoingChatMessageFeaturesToEntityFeatures mOutGoingChatMessageFeaturesToEntityFeatures;

  @Inject
  public OutGoingMessagesEntityMapper(GeoFeatureEntityMapper geoFeatureEntityMapper,
      OutGoingChatMessageFeaturesToEntityFeatures outGoingChatMessageFeaturesToEntityFeatures) {
    mGeoFeatureEntityMapper = geoFeatureEntityMapper;
    mOutGoingChatMessageFeaturesToEntityFeatures = outGoingChatMessageFeaturesToEntityFeatures;
  }

  public OutGoingChatMessage mapToDomain(OutGoingChatMessageEntity messageEntity) {
    if (messageEntity == null) {
      return null;
    }

    return new OutGoingChatMessage(messageEntity.senderId, messageEntity.creationDate,
        createFeaturesFromEntity(messageEntity));
  }

  public OutGoingChatMessageEntity mapToEntity(OutGoingChatMessage message) {
    if (message == null) {
      return null;
    }

    OutGoingChatMessageEntity outGoingChatMessageEntity = createOutGoingChatMessageEntity(message);
    return outGoingChatMessageEntity;
  }

  private MessageFeatureVisitable[] createFeaturesFromEntity(OutGoingChatMessageEntity entity) {
    List<MessageFeatureVisitable> features = new ArrayList<>();

    for (ChatMessageEntity.Feature feature : entity.features) {
      features.add(createFeature(entity, feature));
    }

    return features.toArray(new MessageFeatureVisitable[features.size()]);
  }

  private MessageFeatureVisitable createFeature(OutGoingChatMessageEntity entity,
      ChatMessageEntity.Feature feature) {
    switch (feature) {
      case TEXT:
        return new TextFeature(entity.text);
      case GEO:
        return new GeoFeature(mGeoFeatureEntityMapper.mapToDomain(entity.geoFeatureEntity));
      case IMAGE:
        return convertImageFeatureEntityToDomain(entity.imageFeatureEntity);
      case ALERT:
        return new AlertFeature(convertAlertEntityToDomain(entity.alertFeatureEntity));
      default:
        throw new RuntimeException("Unknown feature found in db entity: " + feature);
    }
  }

  private Style convertStyleEntityToStyle(GeoFeatureEntity.Style style) {
    IconData iconData = new IconData(style.iconId, style.iconTint);
    return new Style(iconData, style.borderColor, style.fillColor, style.borderStyle);
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

  private OutGoingChatMessageEntity createOutGoingChatMessageEntity(OutGoingChatMessage message) {
    OutGoingChatMessageEntity entity = new OutGoingChatMessageEntity();

    entity.senderId = message.getSenderId();
    entity.creationDate = message.getCreatedAt();

    entity = mOutGoingChatMessageFeaturesToEntityFeatures.addFeaturesToEntity(entity, message);
    return entity;
  }
}
