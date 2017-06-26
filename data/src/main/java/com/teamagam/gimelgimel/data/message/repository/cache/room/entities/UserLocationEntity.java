package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_locations")
public class UserLocationEntity {

  @PrimaryKey
  public String id;
  public String user;

  @Embedded
  public LocationSampleEntity location;
}
