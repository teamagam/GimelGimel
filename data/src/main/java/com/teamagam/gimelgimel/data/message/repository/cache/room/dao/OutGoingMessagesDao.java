package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.OutGoingChatMessageEntity;
import java.util.List;

@Dao
public interface OutGoingMessagesDao {
  @Query("SELECT * FROM out_going_messages ORDER BY out_going_id")
  List<OutGoingChatMessageEntity> getMessagesByOutGoingId();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertMessage(OutGoingChatMessageEntity entity);

  @Query("DELETE FROM out_going_messages WHERE out_going_id = (SELECT MIN(out_going_id) FROM out_going_messages);")
  void deleteTopMessage();

  @Query("DELETE FROM out_going_messages")
  void nukeTable();
}
