package com.teamagam.gimelgimel.data.message.adapters;

import com.teamagam.gimelgimel.data.alerts.entity.AlertData;
import com.teamagam.gimelgimel.data.location.adpater.LocationSampleDataAdapter;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageAlertData;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageGeoData;
import com.teamagam.gimelgimel.data.message.entity.MessageImageData;
import com.teamagam.gimelgimel.data.message.entity.MessageTextData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.MessageVectorLayerData;
import com.teamagam.gimelgimel.data.message.entity.UnknownMessageData;
import com.teamagam.gimelgimel.data.message.entity.contents.ImageMetadataData;
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
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import java.net.MalformedURLException;
import java.net.URL;

public class MessageFromDataTransformer implements IMessageDataVisitor {

  private static final Logger sLogger =
      LoggerFactory.create(MessageFromDataTransformer.class.getSimpleName());
  private static final String EMPTY_STRING = "";

  private final GeoEntityDataMapper mGeoEntityDataMapper;
  private final LocationSampleDataAdapter mLocationSampleAdapter;
  private ChatMessage mMessage;
  private VectorLayer mVectorLayer;
  private UserLocation mUserLocation;

  public MessageFromDataTransformer(GeoEntityDataMapper geoEntityDataMapper,
      LocationSampleDataAdapter locationSampleAdapter) {
    mGeoEntityDataMapper = geoEntityDataMapper;
    mLocationSampleAdapter = locationSampleAdapter;
  }

  public ChatMessage transformMessageFromData(MessageData msgData) {
    mMessage = createBaseData(msgData);
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

  private ChatMessage createBaseData(MessageData msgData) {
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
    mMessage.addFeatures(new ImageFeature(metadata.getTime(), EMPTY_STRING, metadata.getRemoteUrl(),
        metadata.getLocalUrl()));

    if (metadata.getLocation() != null) {
      ImageEntity imageEntity =
          mGeoEntityDataMapper.transformIntoImageEntity(message.getMessageId(),
              metadata.getLocation());
      mMessage.addFeatures(new GeoFeature(imageEntity));
    }
  }

  @Override
  public void visit(MessageAlertData message) {
    AlertData alertData = message.getContent();
    Alert alert =
        new Alert(message.getMessageId(), alertData.severity, alertData.text, alertData.source,
            alertData.time);

    mMessage.addFeatures(new AlertFeature(alert));

    if (alertData.location != null) {
      AlertEntity entity =
          mGeoEntityDataMapper.transformIntoAlertEntity(message.getMessageId(), alertData.source,
              alertData.location, alertData.severity);
      mMessage.addFeatures(new GeoFeature(entity));
    }
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
