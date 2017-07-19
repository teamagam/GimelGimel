package com.teamagam.gimelgimel.domain.dynamicLayers.entity;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.util.List;

public class DynamicLayer {
  private final String mId;
  private final String mName;
  private final List<GeoEntity> mEntities;

  public DynamicLayer(String id, String name, List<GeoEntity> entities) {
    mId = id;
    mName = name;
    mEntities = entities;
  }

  @Override
  public String toString() {
    return String.format("DynamicLayer, id: %s\nname: %s\nentities: %s", mId, mName,
        mEntities.toString());
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public List<GeoEntity> getEntities() {
    return mEntities;
  }
}
