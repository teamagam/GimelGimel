package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import io.reactivex.Flowable;
import java.util.List;

@Dao
public interface OutGoingMessagesDao {
  @Query("SELECT * FROM OutGoingMessages ORDER BY creation_date")
  List<ChatMessageEntity> getMessagesByDate();

  @Query("SELECT * FROM OutGoingMessages ORDER BY creation_date DESC LIMIT 1")
  Flowable<ChatMessageEntity> getLatestMessage();

  @Query("SELECT * FROM OutGoingMessages ORDER BY creation_date DESC LIMIT 1")
  ChatMessageEntity getLastMessage();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertMessage(ChatMessageEntity entity);

  @Query("DELETE TOP (1) FROM OutGoingMessages")
  void deleteMessage();

  @Query("DELETE FROM OutGoingMessages")
  void nukeTable();
}
