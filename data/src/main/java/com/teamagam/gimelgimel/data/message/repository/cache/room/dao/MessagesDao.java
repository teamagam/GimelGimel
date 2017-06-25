package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import io.reactivex.Flowable;

@Dao
public interface MessagesDao {

  @Query("SELECT * FROM messages")
  Flowable<ChatMessageEntity> getMessages();

  @Query("SELECT * FROM messages WHERE messageId = :id")
  ChatMessageEntity getMessageById(String id);

  @Query("SELECT * FROM messages ORDER BY creation_date DESC LIMIT 1")
  ChatMessageEntity getLastMessage();

  @Insert
  void insertMessage(ChatMessageEntity entity);
}
