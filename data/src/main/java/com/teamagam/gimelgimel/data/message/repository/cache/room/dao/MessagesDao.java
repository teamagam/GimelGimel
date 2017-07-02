package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import io.reactivex.Flowable;
import java.util.List;

@Dao
public interface MessagesDao {

  @Query("SELECT * FROM messages")
  List<ChatMessageEntity> getMessages();

  @Query("SELECT * FROM messages ORDER BY creation_date DESC LIMIT 1")
  Flowable<ChatMessageEntity> getLatestMessage();

  @Query("SELECT * FROM messages ORDER BY creation_date DESC LIMIT 1")
  ChatMessageEntity getLastMessage();

  @Query("SELECT * FROM messages WHERE messageId = :id")
  ChatMessageEntity getMessageById(String id);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertMessage(ChatMessageEntity entity);

  @Query("DELETE FROM messages")
  void nukeTable();
}
