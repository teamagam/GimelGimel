package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.AlertFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ImageFeatureEntity;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatMessageFeaturesToEntityFeatures implements MessageFeatureVisitor {
  private ChatMessageEntity mChatMessageEntity;
  private GeometryDataMapper mGeometryDataMapper;

  @Inject
  public ChatMessageFeaturesToEntityFeatures(GeometryDataMapper geometryDataMapper) {
    mGeometryDataMapper = geometryDataMapper;
  }

  public ChatMessageEntity addFeaturesToEntity(ChatMessageEntity entity, ChatMessage message) {
    mChatMessageEntity = entity;
    message.accept(this);
    return mChatMessageEntity;
  }

  @Override
  public void visit(TextFeature feature) {
    mChatMessageEntity.text = feature.getText();
    addFeatureType(ChatMessageEntity.Feature.TEXT);
  }

  @Override
  public void visit(GeoFeature feature) {
    GeoEntity geoEntity = feature.getGeoEntity();

    mChatMessageEntity.geoFeatureEntity = new GeoFeatureEntity();
    mChatMessageEntity.geoFeatureEntity.id = geoEntity.getId();
    mChatMessageEntity.geoFeatureEntity.geometry =
        mGeometryDataMapper.transformToDataBySuperclass(geoEntity.getGeometry());
    mChatMessageEntity.geoFeatureEntity.text = geoEntity.getText();

    addFeatureType(ChatMessageEntity.Feature.GEO);
  }

  @Override
  public void visit(ImageFeature feature) {
    ImageFeatureEntity imageFeatureEntity = new ImageFeatureEntity();

    imageFeatureEntity.localUrl = feature.getLocalUrl();
    imageFeatureEntity.remoteUrl = feature.getRemoteUrl();
    imageFeatureEntity.source = feature.getSource();
    imageFeatureEntity.time = feature.getTime();

    mChatMessageEntity.imageFeatureEntity = imageFeatureEntity;
    addFeatureType(ChatMessageEntity.Feature.IMAGE);
  }

  @Override
  public void visit(AlertFeature feature) {
    Alert alert = feature.getAlert();
    AlertFeatureEntity alertEntity = new AlertFeatureEntity();

    alertEntity.alertId = alert.getId();
    alertEntity.severity = alert.getSeverity();
    alertEntity.source = alert.getSource();
    alertEntity.text = alert.getText();
    alertEntity.time = alert.getTime();
    alertEntity.type = alert.getType().ordinal();

    mChatMessageEntity.alertFeatureEntity = alertEntity;
    addFeatureType(ChatMessageEntity.Feature.ALERT);
  }

  private void addFeatureType(ChatMessageEntity.Feature feature) {
    mChatMessageEntity.features.add(feature);
  }
}
