package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.ColumnInfo;
import com.teamagam.geogson.core.model.Point;

public class LocationSampleEntity {

  public Point point;
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
