package com.teamagam.gimelgimel.data.timeplay;

import com.teamagam.geogson.core.model.Coordinates;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.LocationSampleEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import java.util.Date;
import java.util.UUID;

public class Utils {
  public static ChatMessageEntity createGeoChatMessageEntity(String geoEntityId,
      long creationTimestamp) {
    ChatMessageEntity res = new ChatMessageEntity();
    res.messageId = generateRandomId();
    res.senderId = "fake";
    res.creationDate = new Date(creationTimestamp);
    res.geoFeatureEntity = new GeoFeatureEntity();
    res.geoFeatureEntity.geometry = createPoint();
    GeoFeatureEntity.Style style = new GeoFeatureEntity.Style();
    style.iconId = "icon_id";
    res.geoFeatureEntity.style = style;
    res.geoFeatureEntity.id = geoEntityId;
    res.geoFeatureEntity.style = style;
    res.geoFeatureEntity.text = "geo_text";
    return res;
  }

  public static UserLocationEntity createUserLocationEntity(String userId, long timestamp) {
    UserLocationEntity userLocationEntity = new UserLocationEntity();
    userLocationEntity.id = generateRandomId();
    userLocationEntity.user = userId;
    LocationSampleEntity location = new LocationSampleEntity();
    location.time = timestamp;
    location.point = createPoint();
    userLocationEntity.location = location;
    return userLocationEntity;
  }

  private static Point createPoint() {
    return new Point(new SinglePosition(Coordinates.of(30, 30)));
  }

  private static String generateRandomId() {
    return UUID.randomUUID().toString();
  }
}
