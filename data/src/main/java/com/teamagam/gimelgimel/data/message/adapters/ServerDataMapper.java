package com.teamagam.gimelgimel.data.message.adapters;

import com.teamagam.gimelgimel.data.location.adpater.LocationSampleDataAdapter;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.message.entity.ConfirmMessageReadData;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.entity.MessageUserLocationData;
import com.teamagam.gimelgimel.data.message.entity.MessageVectorLayerData;
import com.teamagam.gimelgimel.data.message.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
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

  public MessageData transformToData(ChatMessage message) {
    return getToDataTransformer().transform(message);
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
      return getFromDataTransformer().transformMessageFromData(message);
    } catch (Exception ex) {
      sLogger.w("Couldn't parse message-data with id " + message.getMessageId(), ex);
      return null;
    }
  }

  public VectorLayer transform(MessageVectorLayerData vectorLayerMessage) {
    return getFromDataTransformer().transformVectorLayerFromData(vectorLayerMessage);
  }

  public UserLocation transform(MessageUserLocationData userLocation) {
    return getFromDataTransformer().transformUserLocationFromData(userLocation);
  }

  private MessageToDataTransformer getToDataTransformer() {
    return new MessageToDataTransformer(mGeoEntityDataMapper);
  }

  private MessageFromDataTransformer getFromDataTransformer() {
    return new MessageFromDataTransformer(mGeoEntityDataMapper, mLocationSampleAdapter);
  }
}