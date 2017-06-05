package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import java.util.Date;

/**
 * UserLocation-Type class for {@link Message}
 */
public class MessageUserLocation extends Message {

  private LocationSample mLocationSample;

  public MessageUserLocation(String messageId,
      String senderId,
      Date createdAt,
      LocationSample sample) {
    super(messageId, senderId, createdAt);

    mLocationSample = sample;
  }

  @Override
  public void accept(IMessageVisitor visitor) {
    visitor.visit(this);
  }

  public LocationSample getLocationSample() {
    return mLocationSample;
  }
}
