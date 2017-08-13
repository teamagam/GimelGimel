package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
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
  private FeaturesToEntityFeaturesUtilities mFeaturesToEntityFeaturesUtilities;

  @Inject
  public ChatMessageFeaturesToEntityFeatures(FeaturesToEntityFeaturesUtilities featuresToEntityFeaturesUtilities) {
    mFeaturesToEntityFeaturesUtilities = featuresToEntityFeaturesUtilities;
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
    mChatMessageEntity.geoFeatureEntity =
        mFeaturesToEntityFeaturesUtilities.getGeoFeatureEntity(feature);
    addFeatureType(ChatMessageEntity.Feature.GEO);
  }

  @Override
  public void visit(ImageFeature feature) {
    mChatMessageEntity.imageFeatureEntity =
        mFeaturesToEntityFeaturesUtilities.getImageFeatureEntity(feature);
    addFeatureType(ChatMessageEntity.Feature.IMAGE);
  }

  @Override
  public void visit(AlertFeature feature) {
    mChatMessageEntity.alertFeatureEntity =
        mFeaturesToEntityFeaturesUtilities.getAlertFeatureEntity(feature);
    addFeatureType(ChatMessageEntity.Feature.ALERT);
  }

  private void addFeatureType(ChatMessageEntity.Feature feature) {
    mChatMessageEntity.features.add(feature);
  }
}
