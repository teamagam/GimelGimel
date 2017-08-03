package com.teamagam.gimelgimel.data.message.repository.cache.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import java.util.Set;

@Entity(tableName = "OutGoingMessages", indices = { @Index("creation_date") })
public class OutGoingMessageEntity extends ChatMessageEntity {
  //bla
  public OutGoingMessageEntity(Set<Feature> features) {
    super(features);
  }

  public OutGoingMessageEntity() {
    super();
  }
}