package com.teamagam.gimelgimel.app.sensor.model;

import com.teamagam.gimelgimel.domain.messages.IdentifiedData;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public class SensorMetadataApp implements IdentifiedData {

  private String mId;
  private String mName;

  private GeoEntity mGeoEntity;

  private boolean mIsSelected;

  public SensorMetadataApp(String id, String name, GeoEntity geoEntity) {
    mId = id;
    mName = name;
    mGeoEntity = geoEntity;
    mIsSelected = false;
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public GeoEntity getGeoEntity() {
    return mGeoEntity;
  }

  public void select() {
    mIsSelected = true;
  }

  public void unselect() {
    mIsSelected = false;
  }

  public boolean isSelected() {
    return mIsSelected;
  }
}
