package com.teamagam.gimelgimel.domain.dynamicLayers.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public class DynamicEntity {
  private final GeoEntity mGeoEntity;
  private final String mDescription;

  public DynamicEntity(GeoEntity geoEntity, String description) {
    mGeoEntity = geoEntity;
    mDescription = description;
  }

  public GeoEntity getGeoEntity() {
    return mGeoEntity;
  }

  public String getDescription() {
    return mDescription;
  }

  public String getId() {
    return mGeoEntity.getId();
  }

  @Override
  public String toString() {
    return "DynamicEntity{"
        + "mGeoEntity="
        + mGeoEntity
        + ", mDescription='"
        + mDescription
        + '\''
        + '}';
  }
}