package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.AlertFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ImageFeatureEntity;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import javax.inject.Inject;

public class MessageFeatureEntityMapper {

  private GeoFeatureEntityMapper mGeoFeatureEntityMapper;

  @Inject
  public MessageFeatureEntityMapper(GeoFeatureEntityMapper geoFeatureEntityMapper) {
    mGeoFeatureEntityMapper = geoFeatureEntityMapper;
  }

  public MessageFeatureVisitable createFeature(String text,
      GeoFeatureEntity geoFeatureEntity,
      ImageFeatureEntity imageFeatureEntity,
      AlertFeatureEntity alertFeatureEntity,
      ChatMessageEntity.Feature feature) {
    switch (feature) {
      case TEXT:
        return new TextFeature(text);
      case GEO:
        return new GeoFeature(mGeoFeatureEntityMapper.mapToDomain(geoFeatureEntity));
      case IMAGE:
        return convertImageFeatureEntityToDomain(imageFeatureEntity);
      case ALERT:
        return new AlertFeature(convertAlertEntityToDomain(alertFeatureEntity));
      default:
        throw new RuntimeException("Unknown feature found in db entity: " + feature);
    }
  }

  private ImageFeature convertImageFeatureEntityToDomain(ImageFeatureEntity imageFeatureEntity) {
    return new ImageFeature(imageFeatureEntity.time, imageFeatureEntity.source,
        imageFeatureEntity.remoteUrl, imageFeatureEntity.localUrl);
  }

  private Alert convertAlertEntityToDomain(AlertFeatureEntity alertEntity) {
    return new Alert(alertEntity.alertId, alertEntity.severity, alertEntity.source,
        alertEntity.time, getAlertType(alertEntity.type));
  }

  private Alert.Type getAlertType(int type) {
    return Alert.Type.values()[type];
  }
}
