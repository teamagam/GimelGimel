package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import java.util.List;

public interface MessagesTextSearcher {
  List<ChatMessage> searchMessagesByText(String text);
}
