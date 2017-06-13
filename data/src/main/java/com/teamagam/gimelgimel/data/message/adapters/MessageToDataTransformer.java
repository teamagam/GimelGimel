package com.teamagam.gimelgimel.data.message.adapters;

import com.teamagam.geogson.core.model.Point;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;
import javax.inject.Inject;

public class MessageToDataTransformer implements IMessageFeatureVisitor {

  private final GeoEntityDataMapper mGeoEntityDataMapper;
  private ChatMessage mMessage;
  private TextFeature mTextFeature;
  private GeoFeature mGeoFeature;
  private ImageFeature mImageFeature;
  private AlertFeature mAlertFeature;

  @Inject
  public MessageToDataTransformer(GeoEntityDataMapper geoEntityDataMapper) {
    mGeoEntityDataMapper = geoEntityDataMapper;
  }

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

    addBasicData(buildMessageDataByType());

    return buildMessageDataByType();
  }

  private MessageData buildMessageDataByType() {
    MessageData messageData;

    if (isTextMessage()) {
      messageData = buildTextMessageResponse();
    } else if (isGeoMessage()) {
      messageData = buildGeoMessageResponse();
    } else if (isImageMessage()) {
      messageData = buildImageMessageResponse();
    } else if (isAlertMessage()) {
      throw new RuntimeException("Mapper from MessageAlert to MessageAlertData is not supported!");
    } else {
      throw new RuntimeException("Could not create proper MessageData from existing features");
    }
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
        && mGeoFeature != null
        && mImageFeature == null
        && mAlertFeature == null;
  }

  private boolean isImageMessage() {
    return mTextFeature == null && mImageFeature != null && mAlertFeature == null;
  }

  private boolean isAlertMessage() {
    return mTextFeature != null && mImageFeature == null && mAlertFeature != null;
  }

  private MessageData buildTextMessageResponse() {
    return new MessageTextData(mTextFeature.getText());
  }

  private MessageData buildGeoMessageResponse() {
    GeoContentData content = mGeoEntityDataMapper.transform(mGeoFeature.getGeoEntity());
    content.setText(mTextFeature.getText());

    return new MessageGeoData(content);
  }

  private MessageData buildImageMessageResponse() {
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

  private ImageMetadataData transformMetadataToData(ImageFeature imageFeature) {
    return new ImageMetadataData(imageFeature.getTime(), null, imageFeature.getLocalUrl(),
        imageFeature.getSource());
  }
}
