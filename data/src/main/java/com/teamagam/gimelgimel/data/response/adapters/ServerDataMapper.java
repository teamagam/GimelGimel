package com.teamagam.gimelgimel.data.response.adapters;

import com.teamagam.gimelgimel.data.location.adpater.LocationSampleDataAdapter;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.response.entity.ConfirmMessageReadResponse;
import com.teamagam.gimelgimel.data.response.entity.DynamicLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.UserLocationResponse;
import com.teamagam.gimelgimel.data.response.entity.VectorLayerResponse;
import com.teamagam.gimelgimel.data.response.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.ConfirmMessageRead;
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

  public ServerResponse transformToData(ChatMessage message) {
    return getToDataTransformer().transform(message);
  }

  public ConfirmMessageReadResponse transformToData(ConfirmMessageRead confirm) {
    return new ConfirmMessageReadResponse(confirm.getSenderId(), confirm.getMessageId());
  }

  public UserLocationResponse transformToData(UserLocation userLocation) {
    LocationSampleData locationSampleData =
        mLocationSampleAdapter.transformToData(userLocation.getLocationSample());
    UserLocationResponse response = new UserLocationResponse(locationSampleData);
    response.setSenderId(userLocation.getUser());
    return response;
  }

  public ChatMessage transform(ServerResponse message) {
    try {
      return getFromDataTransformer().transform(message);
    } catch (Exception ex) {
      sLogger.w("Couldn't parse message-data with id " + message.getMessageId(), ex);
      return null;
    }
  }

  public VectorLayer transform(VectorLayerResponse vectorLayerResponse) {
    return getFromDataTransformer().transform(vectorLayerResponse);
  }

  public DynamicLayer transform(DynamicLayerResponse dynamicLayerResponse) {
    return getFromDataTransformer().transform(dynamicLayerResponse);
  }

  public UserLocation transform(UserLocationResponse userLocation) {
    return getFromDataTransformer().transform(userLocation);
  }

  private MessageToDataTransformer getToDataTransformer() {
    return new MessageToDataTransformer(mGeoEntityDataMapper);
  }

  private ResponseTransformer getFromDataTransformer() {
    return new ResponseTransformer(mGeoEntityDataMapper, mLocationSampleAdapter);
  }
}