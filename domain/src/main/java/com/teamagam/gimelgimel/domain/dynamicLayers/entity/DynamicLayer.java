package com.teamagam.gimelgimel.domain.dynamicLayers.entity;

import com.teamagam.gimelgimel.domain.base.repository.IdentifiedData;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.util.List;

public class DynamicLayer implements IdentifiedData {

  private final String mId;
  private final String mName;
  private final String mDescription;
  private final long mTimestamp;
  private final List<DynamicEntity> mEntities;

  public DynamicLayer(String id,
      String name,
      String description, long timestamp, List<DynamicEntity> entities) {
    mId = id;
    mName = name;
    mDescription = description;
    mTimestamp = timestamp;
    mEntities = entities;
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public String getDescription() {
    return mDescription;
  }

  public long getTimestamp() {
    return mTimestamp;
  }

  public List<DynamicEntity> getEntities() {
    return mEntities;
  }

  public DynamicEntity getEntityById(String entityId) {
    for (DynamicEntity entity : mEntities) {
      GeoEntity geoEntity = entity.getGeoEntity();
      if (geoEntity.getId().equals(entityId)) {
        return entity;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "DynamicLayer{"
        + "mId='"
        + mId
        + '\''
        + ", mName='"
        + mName
        + '\''
        + ", mDescription='"
        + mDescription
        + '\''
        + ", mTimestamp="
        + mTimestamp
        + ", mEntities="
        + mEntities
        + '}';
  }
}
