package com.teamagam.gimelgimel.data.response.adapters;

import com.teamagam.gimelgimel.data.alerts.entity.AlertData;
import com.teamagam.gimelgimel.data.location.adpater.LocationSampleDataAdapter;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.response.entity.AlertMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.GeometryMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.TextMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.UnknownResponse;
import com.teamagam.gimelgimel.data.response.entity.UserLocationResponse;
import com.teamagam.gimelgimel.data.response.entity.VectorLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.response.entity.contents.VectorLayerData;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;
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

public class ResponseTransformer implements ResponseVisitor {

  private static final Logger sLogger =
      LoggerFactory.create(ResponseTransformer.class.getSimpleName());
  private static final String EMPTY_STRING = "";

  private final GeoEntityDataMapper mGeoEntityDataMapper;
  private final LocationSampleDataAdapter mLocationSampleAdapter;
  private ChatMessage mMessage;
  private VectorLayer mVectorLayer;
  private UserLocation mUserLocation;

  public ResponseTransformer(GeoEntityDataMapper geoEntityDataMapper,
      LocationSampleDataAdapter locationSampleAdapter) {
    mGeoEntityDataMapper = geoEntityDataMapper;
    mLocationSampleAdapter = locationSampleAdapter;
  }

  public ChatMessage transformMessageFromData(ServerResponse response) {
    mMessage = createBaseData(response);
    response.accept(this);
    return mMessage;
  }

  public VectorLayer transformVectorLayerFromData(VectorLayerResponse vectorLayer) {
    vectorLayer.accept(this);
    return mVectorLayer;
  }

  public UserLocation transformUserLocationFromData(UserLocationResponse userLocation) {
    userLocation.accept(this);
    return mUserLocation;
  }

  private ChatMessage createBaseData(ServerResponse response) {
    return new ChatMessage(response.getMessageId(), response.getSenderId(),
        response.getCreatedAt());
  }

  @Override
  public void visit(TextMessageResponse message) {
    String text = message.getContent();
    mMessage.addFeatures(new TextFeature(text));
  }

  @Override
  public void visit(GeometryMessageResponse message) {
    String text = message.getContent().getText();
    GeoEntity geoEntity =
        mGeoEntityDataMapper.transform(message.getMessageId(), message.getContent());

    mMessage.addFeatures(new TextFeature(text), new GeoFeature(geoEntity));
  }

  @Override
  public void visit(ImageMessageResponse message) {
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
  public void visit(AlertMessageResponse message) {
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

  public void visit(VectorLayerResponse message) {
    VectorLayerData content = message.getContent();
    URL url = tryParseUrl(content.getRemoteUrl());

    mVectorLayer = new VectorLayer(message.getMessageId(), content.getName(), url,
        VectorLayer.Severity.parseCaseInsensitive(content.getSeverity()),
        VectorLayer.Category.parseCaseInsensitive(content.getCategory()), content.getVersion());
  }

  public void visit(UserLocationResponse message) {
    LocationSample locationSample = mLocationSampleAdapter.transform(message.getContent());

    mUserLocation = new UserLocation(message.getSenderId(), locationSample);
  }

  @Override
  public void visit(UnknownResponse message) {
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