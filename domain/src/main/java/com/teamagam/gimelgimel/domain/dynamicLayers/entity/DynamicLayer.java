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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DynamicLayer layer = (DynamicLayer) o;

    if (!mId.equals(layer.mId)) return false;
    if (!mName.equals(layer.mName)) return false;
    return mEntities != null ? mEntities.equals(layer.mEntities) : layer.mEntities == null;
  }

  @Override
  public int hashCode() {
    int result = mId.hashCode();
    result = 31 * result + mName.hashCode();
    result = 31 * result + (mEntities != null ? mEntities.hashCode() : 0);
    return result;
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
