package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

@Entity(tableName = "messages")
public class ChatMessageEntity {

  @PrimaryKey
  public String messageId;

  @ColumnInfo(name = "sender_id")
  public String senderId;

  @ColumnInfo(name = "creation_date")
  public Date creationDate;

  public String text;

  public GeoFeatureEntity geoEntity;

  public ImageFeatureEntity imageFeaureEntity;

  public AlertFeatureEntity alertEntity;

  public EnumSet<Feature> features;

  public ChatMessageEntity() {
  }

  public ChatMessageEntity(Set<Feature> features) {
    this.features = EnumSet.copyOf(features);
  }

  public enum Feature {
    TEXT,
    GEO,
    IMAGE,
    ALERT
  }
}