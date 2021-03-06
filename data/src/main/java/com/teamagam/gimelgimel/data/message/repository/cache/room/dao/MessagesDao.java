package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import java.util.Date;
import java.util.List;

@Dao
public interface MessagesDao {

  @Query("SELECT * FROM messages")
  List<ChatMessageEntity> getMessages();

  @Query("SELECT * FROM messages ORDER BY creation_date DESC LIMIT 1")
  ChatMessageEntity getLastMessage();

  @Query("SELECT * FROM messages WHERE geo <> 'null' AND creation_date <= :maxDate")
  List<ChatMessageEntity> getGeoMessages(Date maxDate);

  @Query("SELECT * FROM messages WHERE messageId = :id")
  ChatMessageEntity getMessageById(String id);

  @Query("SELECT MIN(creation_date) FROM messages WHERE geo <> 'null'")
  Date getMinimumGeoMessageDate();

  @Query("SELECT MAX(creation_date) FROM messages WHERE geo <> 'null'")
  Date getMaximumGeoMessageDate();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertMessage(ChatMessageEntity entity);

  @Query("DELETE FROM messages")
  void nukeTable();
}
