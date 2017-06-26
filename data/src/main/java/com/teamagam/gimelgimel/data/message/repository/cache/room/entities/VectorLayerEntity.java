package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.net.URL;

@Entity(tableName = "layers")
public class VectorLayerEntity {

  @PrimaryKey
  public String id;
  public String layerName;
  public URL url;
  public int severity;
  public int category;
  public int version;
}
