package com.teamagam.gimelgimel.data.response.adapters;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.alerts.entity.AlertData;
import com.teamagam.gimelgimel.data.location.adpater.LocationSampleDataAdapter;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.phases.server.PhasesData;
import com.teamagam.gimelgimel.data.phases.server.PhasesResponse;
import com.teamagam.gimelgimel.data.response.entity.AlertMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.DynamicLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.GeometryMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ImageMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.TextMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.UnknownResponse;
import com.teamagam.gimelgimel.data.response.entity.UserLocationResponse;
import com.teamagam.gimelgimel.data.response.entity.VectorLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.contents.DynamicLayerData;
import com.teamagam.gimelgimel.data.response.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.response.entity.contents.VectorLayerData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
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
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ResponseTransformer {

  private static final Logger sLogger =
      LoggerFactory.create(ResponseTransformer.class.getSimpleName());
  private static final String EMPTY_STRING = "";

  private final GeoEntityDataMapper mGeoEntityDataMapper;
  private final LocationSampleDataAdapter mLocationSampleAdapter;
  private final ResponseTransformer.ResponseToDomainVisitor mTransformVisitor =
      new ResponseToDomainVisitor();
  private ChatMessage mMessage;
  private VectorLayer mVectorLayer;
  private UserLocation mUserLocation;
  private DynamicLayer mDynamicLayer;
  private PhaseLayer mPhaseLayer;

  public ResponseTransformer(GeoEntityDataMapper geoEntityDataMapper,
      LocationSampleDataAdapter locationSampleAdapter) {
    mGeoEntityDataMapper = geoEntityDataMapper;
    mLocationSampleAdapter = locationSampleAdapter;
  }

  public ChatMessage transform(ServerResponse response) {
    mMessage = createBaseData(response);
    response.accept(mTransformVisitor);
    return mMessage;
  }

  public VectorLayer transform(VectorLayerResponse vectorLayer) {
    vectorLayer.accept(mTransformVisitor);
    return mVectorLayer;
  }

  public DynamicLayer transform(DynamicLayerResponse dynamicLayer) {
    dynamicLayer.accept(mTransformVisitor);
    return mDynamicLayer;
  }

  public PhaseLayer transform(PhasesResponse phasesResponse) {
    phasesResponse.accept(mTransformVisitor);
    return mPhaseLayer;
  }

  public UserLocation transform(UserLocationResponse userLocation) {
    userLocation.accept(mTransformVisitor);
    return mUserLocation;
  }

  private ChatMessage createBaseData(ServerResponse response) {
    return new ChatMessage(response.getMessageId(), response.getSenderId(),
        response.getCreatedAt());
  }

  private class ResponseToDomainVisitor implements ResponseVisitor {
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
      mMessage.addFeatures(
          new ImageFeature(metadata.getTime(), EMPTY_STRING, metadata.getRemoteUrl(),
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
          new Alert(message.getMessageId(), alertData.severity, alertData.source, alertData.time);

      mMessage.addFeatures(new AlertFeature(alert), new TextFeature(alertData.text));

      if (alertData.location != null) {
        AlertEntity entity =
            mGeoEntityDataMapper.transformIntoAlertEntity(message.getMessageId(), alertData.source,
                alertData.location, alertData.severity);
        mMessage.addFeatures(new GeoFeature(entity));
      }
    }

    @Override
    public void visit(VectorLayerResponse message) {
      VectorLayerData content = message.getContent();
      URL url = tryParseUrl(content.getRemoteUrl());

      mVectorLayer = new VectorLayer(content.getId(), content.getName(), url,
          VectorLayer.Severity.parseCaseInsensitive(content.getSeverity()),
          VectorLayer.Category.parseCaseInsensitive(content.getCategory()), content.getVersion());
    }

    @Override
    public void visit(DynamicLayerResponse message) {
      mDynamicLayer = dataToDomain(message.getContent(), message.getCreatedAt());
    }

    @Override
    public void visit(PhasesResponse phasesResponse) {
      PhasesData phasesData = phasesResponse.getContent();
      List<DynamicLayer> phases = Lists.transform(Arrays.asList(phasesData.getPhases()),
          dld -> dataToDomain(dld, phasesResponse.getCreatedAt()));
      mPhaseLayer =
          new PhaseLayer(phasesData.getId(), phasesData.getName(), phasesData.getDescription(),
              phasesResponse.getCreatedAt().getTime(), phases);
    }

    @Override
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

    private DynamicLayer dataToDomain(DynamicLayerData dynamicLayerData, Date createdAt) {
      List<GeoContentData> dataGeoEntities = Arrays.asList(dynamicLayerData.getEntities());

      List<DynamicEntity> dynamicEntities = new ArrayList<>();
      for (GeoContentData entity : dataGeoEntities) {
        GeoEntity transform = mGeoEntityDataMapper.transform(entity.getId(), entity);
        dynamicEntities.add(new DynamicEntity(transform, entity.getText()));
      }

      return new DynamicLayer(dynamicLayerData.getId(), dynamicLayerData.getName(),
          dynamicLayerData.getDescription(), createdAt.getTime(), dynamicEntities);
    }
  }
}
