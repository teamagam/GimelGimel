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
import com.teamagam.gimelgimel.data.message.entity.contents.SensorMetadataData;
import com.teamagam.gimelgimel.data.message.entity.contents.VectorLayerData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.entity.GeoAlert;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.ConfirmMessageRead;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.MessageVectorLayer;
import com.teamagam.gimelgimel.domain.messages.entity.contents.GeoImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
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

public class MessageDataMapper {

  private static final Logger sLogger =
      LoggerFactory.create(MessageDataMapper.class.getSimpleName());

  private final LocationSampleDataAdapter mLocationSampleAdapter;
  private final GeoEntityDataMapper mGeoEntityDataMapper;

  @Inject
  public MessageDataMapper(LocationSampleDataAdapter locationSampleAdapter,
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

  // TODO: Remote Data
  public MessageData transformRemoteData() {
    return null;
  }

  public ChatMessage tryTransform(MessageData message) {
    try {
      return new MessageFromDataTransformer().transformFromData(message);
    } catch (Exception ex) {
      sLogger.w("Couldn't parse message-data with id " + message.getMessageId(), ex);
      return null;
    }
  }

  public List<ChatMessage> transform(Collection<MessageData> messageCollection) {
    List<ChatMessage> messageList = new ArrayList<>(20);
    ChatMessage messageModel;
    for (MessageData message : messageCollection) {
      messageModel = tryTransform(message);
      if (messageModel != null) {
        messageList.add(messageModel);
      }
    }

    return messageList;
  }

  private class MessageFromDataTransformer implements IMessageDataVisitor {

    private static final String EMPTY_STRING = "";

    private ChatMessage mMessage;

    private ChatMessage transformFromData(MessageData msgData) {
      setBaseData(msgData);
      msgData.accept(this);
      return mMessage;
    }

    private void setBaseData(MessageData msgData) {
      mMessage =
          new ChatMessage(msgData.getMessageId(), msgData.getSenderId(), msgData.getCreatedAt());
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
      AlertData alertData = message.getContent();
      mMessage.addFeatures(new TextFeature(alertData.text));
      mMessage.addFeatures(
          new AlertFeature(message.getMessageId(), alertData.severity, alertData.source,
              alertData.time));

      if (message.getContent().location != null) {
        AlertEntity entity =
            mGeoEntityDataMapper.transformIntoAlertEntity(message.getMessageId(), alertData.source,
                alertData.location, alertData.severity);
        mMessage.addFeatures(new GeoFeature(entity));
      }
    }

    @Override
    public void visit(UnknownMessageData message) {
      mMessage = new ChatMessage(EMPTY_STRING, EMPTY_STRING, message.getCreatedAt());
    }

    // TODO: Handle this
    @Override
    public void visit(MessageUserLocationData message) {
      LocationSample convertedLocationSample =
          mLocationSampleAdapter.transform(message.getContent());
      mMessage = null;
      /*mMessage = new MessageUserLocation(message.getMessageId(), message.getSenderId(),
          message.getCreatedAt(), convertedLocationSample);*/
    }

    @Override
    public void visit(MessageVectorLayerData message) {
      VectorLayer vl = convertContent(message.getContent());
      URL url = tryParseUrl(message.getContent().getRemoteUrl());
      mMessage = null;
      /*mMessage = new MessageVectorLayer(message.getMessageId(), message.getSenderId(),
          message.getCreatedAt(), vl, url);*/
    }

    private VectorLayer convertContent(VectorLayerData content) {
      return new VectorLayer(content.getId(), content.getName(), content.getVersion(),
          convertSeverity(content.getSeverity()), convertCategory(content.getCategory()));
    }

    private VectorLayer.Severity convertSeverity(String severity) {
      return VectorLayer.Severity.parseCaseInsensitive(severity);
    }

    private VectorLayer.Category convertCategory(String category) {
      return VectorLayer.Category.parseCaseInsensitive(category);
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

      MessageData messageData = null;

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

    // TODO: Move this
    /*@Override
    public void visit(MessageUserLocation message) {
      LocationSampleData locationSampleData =
          mLocationSampleAdapter.transformToData(message.getLocationSample());
      mMessageData = new MessageUserLocationData(locationSampleData);
    }*/

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