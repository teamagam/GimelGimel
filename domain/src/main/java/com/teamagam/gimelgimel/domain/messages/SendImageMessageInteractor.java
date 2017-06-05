package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeoImage;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.domain.messages.entity.contents.GeoImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

@AutoFactory
public class SendImageMessageInteractor extends SendMessageInteractor<MessageImage> {

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
  protected MessageImage createMessage(String senderId) {
    LocationSample lastLocationSample = mLocationRepository.getLastLocationSample();
    if (lastLocationSample == null) {
      return createMessageImage(senderId);
    }

    return createMessageGeoImage(senderId, lastLocationSample.getLocation());
  }

  private MessageImage createMessageImage(String senderId) {
    ImageMetadata imageMetadata = new ImageMetadata(mImageTime, null, mLocalUrl, IMAGE_SOURCE_USER);

    return new MessageImage(null, senderId, null, imageMetadata);
  }

  private MessageGeoImage createMessageGeoImage(String senderId, PointGeometry lastLocation) {
    GeoEntity geoEntity = createGeoEntity(lastLocation);
    GeoImageMetadata geoImageMetadata =
        new GeoImageMetadata(mImageTime, null, mLocalUrl, IMAGE_SOURCE_USER, geoEntity);

    return new MessageGeoImage(null, senderId, null, geoImageMetadata);
  }

  private GeoEntity createGeoEntity(PointGeometry location) {
    return new ImageEntity("not_used", null, location, false);
  }
}
