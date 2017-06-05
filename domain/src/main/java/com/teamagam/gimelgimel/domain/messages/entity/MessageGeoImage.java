package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.contents.GeoImageMetadata;
import java.util.Date;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link Message}
 */
public class MessageGeoImage extends MessageImage implements GeoEntityHolder {

  public MessageGeoImage(String messageId,
      String senderId,
      Date createdAt,
      GeoImageMetadata metadata) {
    super(messageId, senderId, createdAt, metadata);
  }

  @Override
  public GeoEntity getGeoEntity() {
    return ((GeoImageMetadata) getImageMetadata()).getGeoEntity();
  }
}