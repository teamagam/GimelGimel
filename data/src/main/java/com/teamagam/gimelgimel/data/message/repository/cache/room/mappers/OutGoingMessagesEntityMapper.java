package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OutGoingMessagesEntityMapper
    implements EntityMapper<OutGoingChatMessage, OutGoingChatMessageEntity> {

  private MessageFeatureEntityMapper mMessageFeatureEntityMapper;
  private OutGoingChatMessageFeaturesToEntityFeatures mOutGoingChatMessageFeaturesToEntityFeatures;

  @Inject
  public OutGoingMessagesEntityMapper(MessageFeatureEntityMapper messageFeatureEntityMapper,
      OutGoingChatMessageFeaturesToEntityFeatures outGoingChatMessageFeaturesToEntityFeatures) {
    mMessageFeatureEntityMapper = messageFeatureEntityMapper;
    mOutGoingChatMessageFeaturesToEntityFeatures = outGoingChatMessageFeaturesToEntityFeatures;
  }

  public OutGoingChatMessage mapToDomain(OutGoingChatMessageEntity messageEntity) {
    if (messageEntity == null) {
      return null;
    }

    return new OutGoingChatMessage(messageEntity.senderId, createFeaturesFromEntity(messageEntity));
  }

  public OutGoingChatMessageEntity mapToEntity(OutGoingChatMessage message) {
    if (message == null) {
      return null;
    }
    return createOutGoingChatMessageEntity(message);
  }

  private MessageFeatureVisitable[] createFeaturesFromEntity(OutGoingChatMessageEntity entity) {
    List<MessageFeatureVisitable> features = new ArrayList<>();

    for (ChatMessageEntity.Feature feature : entity.features) {
      features.add(mMessageFeatureEntityMapper.createFeature(entity.text, entity.geoFeatureEntity,
          entity.imageFeatureEntity, entity.alertFeatureEntity, feature));
    }

    return features.toArray(new MessageFeatureVisitable[features.size()]);
  }

  private OutGoingChatMessageEntity createOutGoingChatMessageEntity(OutGoingChatMessage message) {
    OutGoingChatMessageEntity entity = new OutGoingChatMessageEntity();
    entity.senderId = message.getSenderId();
    entity = mOutGoingChatMessageFeaturesToEntityFeatures.addFeaturesToEntity(entity, message);
    return entity;
  }
}
