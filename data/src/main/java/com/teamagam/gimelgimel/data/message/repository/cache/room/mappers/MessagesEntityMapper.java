package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessagesEntityMapper implements EntityMapper<ChatMessage, ChatMessageEntity> {

  private MessageFeatureEntityMapper mMessageFeatureEntityMapper;
  private ChatMessageFeaturesToEntityFeatures mFeaturesToEntityFeatures;

  @Inject
  public MessagesEntityMapper(MessageFeatureEntityMapper messageFeatureEntityMapper,
      ChatMessageFeaturesToEntityFeatures featuresToEntityFeatures) {
    mMessageFeatureEntityMapper = messageFeatureEntityMapper;
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

    if (entity.features != null) {
      for (ChatMessageEntity.Feature feature : entity.features) {
        MessageFeatureVisitable domainFeature =
            mMessageFeatureEntityMapper.createFeature(entity.text, entity.geoFeatureEntity,
                entity.imageFeatureEntity, entity.alertFeatureEntity, feature);
        features.add(domainFeature);
      }
    }

    return features.toArray(new MessageFeatureVisitable[features.size()]);
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
