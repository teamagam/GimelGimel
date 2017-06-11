package com.teamagam.gimelgimel.data.message.adapters;

import com.teamagam.geogson.core.model.Point;
import com.teamagam.gimelgimel.data.alerts.entity.AlertData;
import com.teamagam.gimelgimel.data.location.adpater.LocationSampleDataAdapter;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.message.entity.ConfirmMessageReadData;
import com.teamagam.gimelgimel.data.message.entity.MessageAlertData;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.MessageVectorLayerData;
import com.teamagam.gimelgimel.data.message.entity.UnknownMessageData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.message.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.data.message.entity.contents.VectorLayerData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.ConfirmMessageRead;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

public class ServerDataMapper {

  private static final Logger sLogger =
      LoggerFactory.create(ServerDataMapper.class.getSimpleName());

  private final LocationSampleDataAdapter mLocationSampleAdapter;
  private final GeoEntityDataMapper mGeoEntityDataMapper;

  @Inject
  public ServerDataMapper(LocationSampleDataAdapter locationSampleAdapter,
      GeoEntityDataMapper geoEntityDataMapper) {
    mLocationSampleAdapter = locationSampleAdapter;
    mGeoEntityDataMapper = geoEntityDataMapper;
  }

  public MessageData transformToData(ChatMessage message) {
    return new MessageToDataTransformer().transform(message);
  }

  public ConfirmMessageReadData transformToData(ConfirmMessageRead confirm) {
    return new ConfirmMessageReadData(confirm.getSenderId(), confirm.getMessageId());
  }

  public MessageUserLocationData transformToData(UserLocation userLocation) {
    LocationSampleData locationSampleData =
        mLocationSampleAdapter.transformToData(userLocation.getLocationSample());
    return new MessageUserLocationData(locationSampleData);
  }

  public ChatMessage transform(MessageData message) {
    try {
      return new MessageFromDataTransformer().transformMessageFromData(message);
    } catch (Exception ex) {
      sLogger.w("Couldn't parse message-data with id " + message.getMessageId(), ex);
      return null;
    }
  }

  public List<ChatMessage> transform(Collection<MessageData> messageCollection) {
    List<ChatMessage> messageList = new ArrayList<>(20);
    ChatMessage messageModel;
    for (MessageData message : messageCollection) {
      messageModel = transform(message);
      if (messageModel != null) {
        messageList.add(messageModel);
      }
    }

    return messageList;
  }

  public VectorLayer transform(MessageVectorLayerData vectorLayerMessage) {
    return new MessageFromDataTransformer().transformVectorLayerFromData(vectorLayerMessage);
  }

  public UserLocation transform(MessageUserLocationData userLocation) {
    return new MessageFromDataTransformer().transformUserLocationFromData(userLocation);
  }

  public ChatAlert transform(MessageAlertData message) {
    try {
      return new MessageFromDataTransformer().transformChatAlertFromData(message);
    } catch (Exception ex) {
      sLogger.w("Couldn't parse message-data with id " + message.getMessageId(), ex);
      return null;
    }
  }

  private class MessageFromDataTransformer implements IMessageDataVisitor {

    private static final String EMPTY_STRING = "";

    private ChatMessage mMessage;
    private VectorLayer mVectorLayer;
    private UserLocation mUserLocation;
    private ChatAlert mChatAlert;

    private ChatMessage transformMessageFromData(MessageData msgData) {
      mMessage = setBaseData(msgData);
      msgData.accept(this);
      return mMessage;
    }

    public VectorLayer transformVectorLayerFromData(MessageVectorLayerData vectorLayer) {
      vectorLayer.accept(this);
      return mVectorLayer;
    }

    public UserLocation transformUserLocationFromData(MessageUserLocationData userLocation) {
      userLocation.accept(this);
      return mUserLocation;
    }

    public ChatAlert transformChatAlertFromData(MessageAlertData alert) {
      alert.accept(this);
      return mChatAlert;
    }

    private ChatMessage setBaseData(MessageData msgData) {
      return new ChatMessage(msgData.getMessageId(), msgData.getSenderId(), msgData.getCreatedAt());
    }

    @Override
    public void visit(MessageTextData message) {
      String text = message.getContent();
      mMessage.addFeatures(new TextFeature(text));
    }

    @Override
    public void visit(MessageGeoData message) {
      String text = message.getContent().getText();
      GeoEntity geoEntity =
          mGeoEntityDataMapper.transform(message.getMessageId(), message.getContent());

      mMessage.addFeatures(new TextFeature(text), new GeoFeature(geoEntity));
    }

    @Override
    public void visit(MessageImageData message) {
      ImageMetadataData metadata = message.getContent();
      mMessage.addFeatures(
          new ImageFeature(metadata.getTime(), EMPTY_STRING, metadata.getRemoteUrl(),
              metadata.getLocalUrl()));

      if (message.getContent().getLocation() != null) {
        ImageEntity imageEntity =
            mGeoEntityDataMapper.transformIntoImageEntity(message.getMessageId(),
                metadata.getLocation());
        mMessage.addFeatures(new GeoFeature(imageEntity));
      }
    }

    @Override
    public void visit(MessageAlertData message) {
      ChatMessage chatMessage = setBaseData(message);
      AlertData alertData = message.getContent();
      chatMessage.addFeatures(new TextFeature(alertData.text));
      chatMessage.addFeatures(new AlertFeature(message.getMessageId()));

      if (message.getContent().location != null) {
        AlertEntity entity =
            mGeoEntityDataMapper.transformIntoAlertEntity(message.getMessageId(), alertData.source,
                alertData.location, alertData.severity);
        chatMessage.addFeatures(new GeoFeature(entity));
      }

      Alert alert =
          new Alert(message.getMessageId(), alertData.severity, alertData.text, alertData.source,
              alertData.time);

      mChatAlert = new ChatAlert(chatMessage, alert);
    }

    public void visit(MessageVectorLayerData message) {
      VectorLayerData content = message.getContent();
      URL url = tryParseUrl(content.getRemoteUrl());

      mVectorLayer = new VectorLayer(message.getMessageId(), content.getName(), url,
          VectorLayer.Severity.parseCaseInsensitive(content.getSeverity()),
          VectorLayer.Category.parseCaseInsensitive(content.getCategory()), content.getVersion());
    }

    public void visit(MessageUserLocationData message) {
      LocationSample locationSample = mLocationSampleAdapter.transform(message.getContent());

      mUserLocation = new UserLocation(message.getSenderId(), locationSample);
    }

    @Override
    public void visit(UnknownMessageData message) {
      mMessage = new ChatMessage(EMPTY_STRING, EMPTY_STRING, message.getCreatedAt());
    }

    private URL tryParseUrl(String remoteUrl) {
      try {
        return new URL(remoteUrl);
      } catch (MalformedURLException e) {
        sLogger.e("Couldn't parse URL out of " + remoteUrl, e);
      }
      return null;
    }
  }

  private class MessageToDataTransformer implements IMessageFeatureVisitor {

    private ChatMessage mMessage;
    private TextFeature mTextFeature;
    private GeoFeature mGeoFeature;
    private ImageFeature mImageFeature;
    private AlertFeature mAlertFeature;

    @Override
    public void visit(TextFeature feature) {
      mTextFeature = feature;
    }

    @Override
    public void visit(GeoFeature feature) {
      mGeoFeature = feature;
    }

    @Override
    public void visit(ImageFeature feature) {
      mImageFeature = feature;
    }

    @Override
    public void visit(AlertFeature feature) {
      mAlertFeature = feature;
    }

    public MessageData transform(ChatMessage message) {
      initFeatures();

      mMessage = message;
      mMessage.accept(this);

      MessageData messageData;

      if (isTextMessage()) {
        messageData = buildTextMessage();
      } else if (isGeoMessage()) {
        messageData = buildGeoMessage();
      } else if (isImageMessage()) {
        messageData = buildImageMessage();
      } else if (isAlertMessage()) {
        throw new RuntimeException("Mapper from MessageAlert to MessageAlertData is not supported");
      } else {
        throw new RuntimeException("Could not create proper MessageData from existing features");
      }

      addBasicData(messageData);

      return messageData;
    }

    private void initFeatures() {
      mTextFeature = null;
      mGeoFeature = null;
      mImageFeature = null;
      mAlertFeature = null;
    }

    private boolean isTextMessage() {
      return mTextFeature != null
          && mGeoFeature == null
          && mImageFeature == null
          && mAlertFeature == null;
    }

    private boolean isGeoMessage() {
      return mTextFeature != null
          && mGeoFeature == null
          && mImageFeature == null
          && mAlertFeature == null;
    }

    private boolean isImageMessage() {
      return mTextFeature == null && mImageFeature != null && mAlertFeature == null;
    }

    private boolean isAlertMessage() {
      return mTextFeature != null && mImageFeature == null && mAlertFeature != null;
    }

    private MessageData buildTextMessage() {
      return new MessageTextData(mTextFeature.getText());
    }

    private MessageData buildGeoMessage() {
      GeoContentData content = mGeoEntityDataMapper.transform(mGeoFeature.getGeoEntity());
      content.setText(mTextFeature.getText());

      return new MessageGeoData(content);
    }

    private MessageData buildImageMessage() {
      ImageMetadataData imageMetadata = transformMetadataToData(mImageFeature);
      if (mGeoFeature != null) {
        GeoContentData content = mGeoEntityDataMapper.transform(mGeoFeature.getGeoEntity());
        imageMetadata.setLocation((Point) content.getGeometry());
      }

      return new MessageImageData(imageMetadata);
    }

    private void addBasicData(MessageData messageData) {
      messageData.setCreatedAt(mMessage.getCreatedAt());
      messageData.setMessageId(mMessage.getMessageId());
      messageData.setSenderId(mMessage.getSenderId());
    }

    //those should'nt be here`
    private ImageMetadataData transformMetadataToData(ImageFeature imageFeature) {
      return new ImageMetadataData(imageFeature.getTime(), null, imageFeature.getLocalUrl(),
          imageFeature.getSource());
    }
  }
}