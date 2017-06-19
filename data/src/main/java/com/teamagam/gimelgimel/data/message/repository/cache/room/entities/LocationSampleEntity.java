package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.ColumnInfo;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public class LocationSampleEntity {

  public PointGeometry point;
  public long time;
  public String provider;

  @ColumnInfo(name = "has_speed")
  public boolean hasSpeed;
  public float speed;

  @ColumnInfo(name = "has_bearing")
  public boolean hasBearing;
  public float bearing;

  @ColumnInfo(name = "has_accuracy")
  public boolean hasAccuracy;
  public float accuracy;
}
