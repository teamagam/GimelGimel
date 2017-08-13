package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OutGoingChatMessageFeaturesToEntityFeatures implements MessageFeatureVisitor {

  private FeaturesToEntityFeaturesUtilities mFeaturesToEntityFeaturesUtilities;
  private OutGoingChatMessageEntity mOutGoingChatMessageEntity;

  @Inject
  public OutGoingChatMessageFeaturesToEntityFeatures(FeaturesToEntityFeaturesUtilities featuresToEntityFeaturesUtilities) {
    mFeaturesToEntityFeaturesUtilities = featuresToEntityFeaturesUtilities;
  }

  public OutGoingChatMessageEntity addFeaturesToEntity(OutGoingChatMessageEntity entity,
      OutGoingChatMessage message) {
    mOutGoingChatMessageEntity = entity;
    message.accept(this);
    return mOutGoingChatMessageEntity;
  }

  @Override
  public void visit(TextFeature feature) {
    mOutGoingChatMessageEntity.text = feature.getText();
    addFeatureType(ChatMessageEntity.Feature.TEXT);
  }

  @Override
  public void visit(GeoFeature feature) {
    mOutGoingChatMessageEntity.geoFeatureEntity =
        mFeaturesToEntityFeaturesUtilities.getGeoFeatureEntity(feature);
    addFeatureType(ChatMessageEntity.Feature.GEO);
  }

  @Override
  public void visit(ImageFeature feature) {
    mOutGoingChatMessageEntity.imageFeatureEntity =
        mFeaturesToEntityFeaturesUtilities.getImageFeatureEntity(feature);
    addFeatureType(ChatMessageEntity.Feature.IMAGE);
  }

  @Override
  public void visit(AlertFeature feature) {
    mOutGoingChatMessageEntity.alertFeatureEntity =
        mFeaturesToEntityFeaturesUtilities.getAlertFeatureEntity(feature);
    addFeatureType(ChatMessageEntity.Feature.ALERT);
  }

  private void addFeatureType(ChatMessageEntity.Feature feature) {
    mOutGoingChatMessageEntity.features.add(feature);
  }
}
