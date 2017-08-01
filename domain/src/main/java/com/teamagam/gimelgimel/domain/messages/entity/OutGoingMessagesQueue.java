package com.teamagam.gimelgimel.domain.messages.entity;

import io.reactivex.Observable;

public interface OutGoingMessagesQueue {
  void addMessage(ChatMessage chatMessage);

  ChatMessage getMessage();

  boolean isEmpty();

  void removeMessage();

  Observable<ChatMessage> getObservable();

  void addAllMessagesToObservable();
}
