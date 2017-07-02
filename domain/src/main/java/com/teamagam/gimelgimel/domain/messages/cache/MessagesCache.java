package com.teamagam.gimelgimel.domain.messages.cache;

import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import io.reactivex.Observable;

public interface MessagesCache {
  Observable<ChatMessage> getMessages();

  void insertMessage(ChatMessage message);

  ChatMessage getMessageById(String messageId);

  ChatMessage getLastMessage();
}
