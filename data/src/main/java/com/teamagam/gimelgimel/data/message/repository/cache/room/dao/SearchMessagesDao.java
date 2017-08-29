package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import java.util.List;

@Dao
public interface SearchMessagesDao {
  @Query("SELECT * FROM messages WHERE text LIKE (:searchText)")
  List<ChatMessageEntity> getMessagesMatchingSearch(String searchText);
}