package com.teamagam.gimelgimel.data.response.adapters;

import com.teamagam.geogson.core.model.Point;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.GeometryMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.TextMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.contents.ImageMetadataData;
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

  public ServerResponse transform(ChatMessage message) {
    initFeatures();

    mMessage = message;
    mMessage.accept(this);

    addBasicData(buildMessageDataByType());

    return buildMessageDataByType();
  }

  private ServerResponse buildMessageDataByType() {
    ServerResponse response;

    if (isTextMessage()) {
      response = buildTextMessageResponse();
    } else if (isGeoMessage()) {
      response = buildGeoMessageResponse();
    } else if (isImageMessage()) {
      response = buildImageMessageResponse();
    } else if (isAlertMessage()) {
      throw new RuntimeException("Mapper from MessageAlert to MessageAlertData is not supported!");
    } else {
      throw new RuntimeException("Could not create proper MessageData from existing features");
    }
    return serverResponse;
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

  private ServerResponse buildTextMessageResponse() {
    return new TextMessageResponse(mTextFeature.getText());
  }

  private ServerResponse buildGeoMessageResponse() {
    GeoContentData content = mGeoEntityDataMapper.transform(mGeoFeature.getGeoEntity());
    content.setText(mTextFeature.getText());

    return new GeometryMessageResponse(content);
  }

  private ServerResponse buildImageMessageResponse() {
    ImageMetadataData imageMetadata = transformMetadataToData(mImageFeature);
    if (mGeoFeature != null) {
      GeoContentData content = mGeoEntityDataMapper.transform(mGeoFeature.getGeoEntity());
      imageMetadata.setLocation((Point) content.getGeometry());
    }

    return new ImageMessageResponse(imageMetadata);
  }

  private void addBasicData(ServerResponse response) {
    response.setCreatedAt(mMessage.getCreatedAt());
    response.setMessageId(mMessage.getMessageId());
    response.setSenderId(mMessage.getSenderId());
  }

  private ImageMetadataData transformMetadataToData(ImageFeature imageFeature) {
    return new ImageMetadataData(imageFeature.getTime(), null, imageFeature.getLocalUrl(),
        imageFeature.getSource());
  }
}
