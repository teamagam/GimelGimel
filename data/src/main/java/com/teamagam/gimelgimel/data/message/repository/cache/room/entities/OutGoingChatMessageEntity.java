package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity.Feature;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

@Entity(inheritSuperIndices = true, tableName = "out_going_messages", indices = {
    @Index("creation_date")
})
public class OutGoingChatMessageEntity {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "out_going_id")
  public int outGoingMessageId;

  @ColumnInfo(name = "sender_id")
  public String senderId;

  @ColumnInfo(name = "creation_date")
  public Date creationDate;

  public String text;

  @ColumnInfo(name = "geo")
  public GeoFeatureEntity geoFeatureEntity;

  @ColumnInfo(name = "image")
  public ImageFeatureEntity imageFeatureEntity;

  @ColumnInfo(name = "alert")
  public AlertFeatureEntity alertFeatureEntity;

  public EnumSet<Feature> features;

  public OutGoingChatMessageEntity() {
    features = EnumSet.noneOf(Feature.class);
  }

  public OutGoingChatMessageEntity(Set<Feature> features) {
    this.features = EnumSet.copyOf(features);
  }
}
