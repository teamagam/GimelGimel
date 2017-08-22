package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

@AutoFactory
public class QueueImageMessageForSendingInteractor extends QueueMessageForSendingInteractor {

  private static final String IMAGE_SOURCE_USER = "User";

  private final LocationRepository mLocationRepository;
  private long mImageTime;
  private String mLocalUrl;

  QueueImageMessageForSendingInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided UserPreferencesRepository userPreferences,
      @Provided OutGoingMessagesQueue outGoingMessagesQueue,
      @Provided LocationRepository locationRepository,
      long imageTime,
      String localUrl) {
    super(threadExecutor, userPreferences, outGoingMessagesQueue);
    mLocationRepository = locationRepository;
    mImageTime = imageTime;
    mLocalUrl = localUrl;
  }

  @Override
  protected OutGoingChatMessage createMessage(String senderId) {
    OutGoingChatMessage outGoingChatMessage = new OutGoingChatMessage(senderId,
        new ImageFeature(mImageTime, IMAGE_SOURCE_USER, null, mLocalUrl));
    outGoingChatMessage = addGeoIfNeeded(outGoingChatMessage);
    return outGoingChatMessage;
  }

  private OutGoingChatMessage addGeoIfNeeded(OutGoingChatMessage chatMessage) {
    LocationSample lastLocationSample = mLocationRepository.getLastLocationSample();
    if (lastLocationSample != null) {
      GeoEntity geoEntity = createGeoEntity(lastLocationSample.getLocation());

      chatMessage.addFeatures(new GeoFeature(geoEntity));
    }

    return chatMessage;
  }

  private GeoEntity createGeoEntity(PointGeometry location) {
    return new ImageEntity("not_used", location, false);
  }
}
