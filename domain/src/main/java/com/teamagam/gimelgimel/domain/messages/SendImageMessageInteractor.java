package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

@AutoFactory
public class SendImageMessageInteractor extends SendMessageInteractor {

  private static final String IMAGE_SOURCE_USER = "User";

  private final LocationRepository mLocationRepository;
  private long mImageTime;
  private String mLocalUrl;

  SendImageMessageInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided UserPreferencesRepository userPreferences,
      @Provided MessagesRepository messagesRepository,
      @Provided LocationRepository locationRepository,
      @Provided MessageNotifications messageNotifications,
      long imageTime,
      String localUrl) {
    super(threadExecutor, userPreferences, messageNotifications, messagesRepository);
    mLocationRepository = locationRepository;
    mImageTime = imageTime;
    mLocalUrl = localUrl;
  }

  @Override
  protected ChatMessage createMessage(String senderId) {
    ChatMessage chatMessage = new ChatMessage(null, senderId, null,
        new ImageFeature(mImageTime, IMAGE_SOURCE_USER, null, mLocalUrl));

    chatMessage = addGeoIfNeeded(chatMessage);

    return chatMessage;
  }

  private ChatMessage addGeoIfNeeded(ChatMessage chatMessage) {
    LocationSample lastLocationSample = mLocationRepository.getLastLocationSample();
    if (lastLocationSample != null) {
      GeoEntity geoEntity = createGeoEntity(lastLocationSample.getLocation());

      chatMessage.addFeatures(new GeoFeature(geoEntity));
    }

    return chatMessage;
  }

  private GeoEntity createGeoEntity(PointGeometry location) {
    return new ImageEntity("not_used", null, location, false);
  }
}
