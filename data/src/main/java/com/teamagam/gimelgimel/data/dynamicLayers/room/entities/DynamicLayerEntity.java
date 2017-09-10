package com.teamagam.gimelgimel.data.dynamicLayers.room.entities;

import android.arch.persistence.room.Entity;
import java.util.Arrays;

@Entity(tableName = "dynamic_layers", primaryKeys = { "id", "timestamp" })
public class DynamicLayerEntity {

  public String id;
  public String name;
  public String description;
  public long timestamp;
  public DynamicEntityDbEntity[] entities;

  @Override
  public String toString() {
    return String.format("DynamicLayerEntity, id: %s\ntimestamp: %s\nname: %s\nentities: %s", id,
        timestamp, name, Arrays.toString(entities));
  }
}
