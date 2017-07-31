package com.teamagam.gimelgimel.data.dynamicLayers.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import java.util.Arrays;

@Entity(tableName = "dynamic_layers")
public class DynamicLayerEntity {
  @PrimaryKey
  public String id;
  public String name;
  public GeoFeatureEntity[] entities;

  @Override
  public String toString() {
    return String.format("DynamicLayerEntity, id: %s\nname: %s\nentities: %s", id, name,
        Arrays.toString(entities));
  }
}
