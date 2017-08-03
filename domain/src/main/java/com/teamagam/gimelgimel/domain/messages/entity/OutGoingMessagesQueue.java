package com.teamagam.gimelgimel.domain.messages.entity;

import io.reactivex.Observable;

public interface OutGoingMessagesQueue {
  void addMessage(ChatMessage chatMessage);

  ChatMessage getTopMessage();

  boolean isEmpty();

  void removeTopMessage();

  Observable<ChatMessage> getObservable();

  void switchTopMessageToQueueStart();
}
