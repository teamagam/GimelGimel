package com.teamagam.gimelgimel.data.dynamicLayers.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;

@Entity(tableName = "dynamic_entities")
public class DynamicEntityDbEntity {

  @PrimaryKey(autoGenerate = true)
  public long id;
  public String description;
  public GeoFeatureEntity geoFeatureEntity;

  @Override
  public String toString() {
    return "DynamicEntityDbEntity{"
        + "id="
        + id
        + ", description='"
        + description
        + '\''
        + ", geoFeatureEntity="
        + geoFeatureEntity
        + '}';
  }
}