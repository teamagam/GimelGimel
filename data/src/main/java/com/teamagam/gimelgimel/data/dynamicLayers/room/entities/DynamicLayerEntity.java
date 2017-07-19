package com.teamagam.gimelgimel.data.dynamicLayers.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "dynamic_layers")
public class DynamicLayerEntity {
  @PrimaryKey
  public String id;
  public String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DynamicLayerEntity that = (DynamicLayerEntity) o;

    if (!id.equals(that.id)) return false;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }
}
