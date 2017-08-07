package com.teamagam.gimelgimel.domain.messages.entity;

import io.reactivex.Observable;

public interface OutGoingMessagesQueue {
  void addMessage(OutGoingChatMessage chatMessage);

  OutGoingChatMessage getTopMessage();

  boolean isEmpty();

  void removeTopMessage();

  Observable<OutGoingChatMessage> getObservable();

  void switchTopMessageToQueueStart();
}