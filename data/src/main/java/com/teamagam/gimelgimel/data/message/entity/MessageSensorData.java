package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.contents.SensorMetadataData;
import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;

public class MessageSensorData extends MessageData<SensorMetadataData> {

  public MessageSensorData(SensorMetadataData sensorMetadataData) {
    super(MessageData.SENSOR);
    mContent = sensorMetadataData;
  }

  @Override
  public void accept(IMessageDataVisitor visitor) {
    visitor.visit(this);
  }
}
