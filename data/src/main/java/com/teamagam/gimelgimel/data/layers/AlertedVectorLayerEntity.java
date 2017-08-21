package com.teamagam.gimelgimel.data.layers;

import android.arch.persistence.room.Entity;

@Entity(primaryKeys = { "id", "version" }, tableName = "alerted_vls")
public class AlertedVectorLayerEntity {

  public String id;
  public int version;
}