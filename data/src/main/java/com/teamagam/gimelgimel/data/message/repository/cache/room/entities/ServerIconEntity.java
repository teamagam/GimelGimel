package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.net.URL;

@Entity(tableName = "server_icons")
public class ServerIconEntity {

  @PrimaryKey
  public String id;
  public URL url;

  @ColumnInfo(name = "displayNameEng")
  public String displayNameEng;

  @ColumnInfo(name = "displayNameHeb")
  public String displayNameHeb;
}
