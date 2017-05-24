package com.teamagam.gimelgimel.domain.messages.entity.contents;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;

public class SensorMetadata {

  private String mId;
  private String mName;
  private SensorEntity mGeoEntity;

  public SensorMetadata(String id, String name, SensorEntity geoEntity) {
    mId = id;
    mName = name;
    mGeoEntity = geoEntity;
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public SensorEntity getGeoEntity() {
    return mGeoEntity;
  }
}
