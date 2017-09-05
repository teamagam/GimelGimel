package com.teamagam.gimelgimel.domain.dynamicLayers.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.util.List;

public class DynamicLayer {

  private final String mId;
  private final String mName;
  private final String mDescription;
  private final long mTimestamp;
  private final List<GeoEntity> mEntities;

  public DynamicLayer(String id,
      String name,
      String description,
      long timestamp,
      List<GeoEntity> entities) {
    mId = id;
    mName = name;
    mDescription = description;
    mTimestamp = timestamp;
    mEntities = entities;
  }

  @Override
  public String toString() {
    String entities = mEntities != null ? mEntities.toString() : "[]";
    return String.format("\nDynamicLayer, id: %s\nname: %s\nentities: %s", mId, mName, entities);
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

  public List<GeoEntity> getEntities() {
    return mEntities;
  }

  public GeoEntity getEntityById(String entityId) {
    for (GeoEntity entity : mEntities) {
      if (entity.getId().equals(entityId)) {
        return entity;
      }
    }
    return null;
  }
}
