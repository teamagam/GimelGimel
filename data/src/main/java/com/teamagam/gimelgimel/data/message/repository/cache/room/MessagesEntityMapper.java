package com.teamagam.gimelgimel.data.message.repository.cache.room;

import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.AlertFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ImageFeatureEntity;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MessagesEntityMapper {

  @Inject
  public MessagesEntityMapper() {
  }

  public ChatMessage convertToDomain(ChatMessageEntity message) {
    return new ChatMessage(message.messageId, message.senderId, message.creationDate,
        createFeaturesFromEntity(message));
  }

  public ChatMessageEntity convertToEntity(ChatMessage message) {
    ChatMessageEntity entity = new ChatMessageEntity();

    entity.messageId = message.getMessageId();
    entity.senderId = message.getSenderId();
    entity.creationDate = message.getCreatedAt();

    return new ChatMessageToEntityVisitor().addFeaturesToEntity(entity, message);
  }

  private IMessageFeatureVisitable[] createFeaturesFromEntity(ChatMessageEntity entity) {
    List<IMessageFeatureVisitable> features = new ArrayList<>();

    for (ChatMessageEntity.Feature feature : entity.features) {
      switch (feature) {
        case TEXT:
          features.add(new TextFeature(entity.text));
          break;
        case GEO:
          features.add(new GeoFeature(convertGeoEntityToDomain(entity.geoEntity)));
          break;
        case IMAGE:
          features.add(convertImageFeatureEntityToDomain(entity.imageFeaureEntity));
          break;
        case ALERT:
          features.add(new AlertFeature(convertAlertEntityToDomain(entity.alertEntity)));
          break;
        default:
          throw new RuntimeException("Unknown feature found in db entity: " + feature);
      }
    }

    return features.toArray(new IMessageFeatureVisitable[features.size()]);
  }

  private GeoEntity convertGeoEntityToDomain(GeoFeatureEntity geoEntity) {
    return new PointEntity("TestId", "Test", new PointGeometry(32.33, 33.24),
        new PointSymbol(false, PointSymbol.POINT_TYPE_GENERAL));
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

  private class ChatMessageToEntityVisitor implements IMessageFeatureVisitor {
    private ChatMessageEntity mChatMessageEntity;

    public ChatMessageEntity addFeaturesToEntity(ChatMessageEntity entity, ChatMessage message) {
      mChatMessageEntity = entity;
      message.accept(this);
      return mChatMessageEntity;
    }

    @Override
    public void visit(TextFeature feature) {
      mChatMessageEntity.text = feature.getText();
      mChatMessageEntity.features.add(ChatMessageEntity.Feature.TEXT);
    }

    @Override
    public void visit(GeoFeature feature) {
      GeoEntity geoEntity = feature.getGeoEntity();
      mChatMessageEntity.geoEntity = new GeoFeatureEntity();
      mChatMessageEntity.geoEntity.locationType = "";

      mChatMessageEntity.features.add(ChatMessageEntity.Feature.GEO);
    }

    @Override
    public void visit(ImageFeature feature) {
      ImageFeatureEntity imageFeatureEntity = new ImageFeatureEntity();
      imageFeatureEntity.localUrl = feature.getLocalUrl();
      imageFeatureEntity.remoteUrl = feature.getRemoteUrl();
      imageFeatureEntity.source = feature.getSource();
      imageFeatureEntity.time = feature.getTime();

      mChatMessageEntity.imageFeaureEntity = imageFeatureEntity;
      mChatMessageEntity.features.add(ChatMessageEntity.Feature.IMAGE);
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

      mChatMessageEntity.alertEntity = alertEntity;
      mChatMessageEntity.features.add(ChatMessageEntity.Feature.ALERT);
    }
  }
}
