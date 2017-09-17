package com.teamagam.gimelgimel.data.phases.repository.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import java.util.Arrays;

@Entity(tableName = "phase_layers")
public class PhaseLayerEntity {

  @PrimaryKey
  public String id;
  public String description;
  public String name;
  public long timestamp;
  public DynamicLayerEntity[] phases;

  @Override
  public String toString() {
    return "PhaseLayerEntity{"
        + "id='"
        + id
        + '\''
        + ", description='"
        + description
        + '\''
        + ", name='"
        + name
        + '\''
        + ", timestamp="
        + timestamp
        + ", phases="
        + Arrays.toString(phases)
        + '}';
  }
}
