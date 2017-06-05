package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import java.util.Date;

public class MessageSensor extends Message implements GeoEntityHolder {

  private SensorMetadata mSensorData;

  public MessageSensor(String messageId,
      String senderId,
      Date createdAt,
      SensorMetadata sensorData) {
    super(messageId, senderId, createdAt);
    mSensorData = sensorData;
  }

  public SensorMetadata getSensorMetadata() {
    return mSensorData;
  }

  @Override
  public GeoEntity getGeoEntity() {
    return mSensorData.getGeoEntity();
  }

  @Override
  public void accept(IMessageVisitor visitor) {
    visitor.visit(this);
  }
}