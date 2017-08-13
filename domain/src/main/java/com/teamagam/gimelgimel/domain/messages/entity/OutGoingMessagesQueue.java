package com.teamagam.gimelgimel.domain.messages.entity;

import io.reactivex.Observable;

public interface OutGoingMessagesQueue {
  void addMessage(OutGoingChatMessage chatMessage);

  OutGoingChatMessage getTopMessage();

  void removeTopMessage();

  Observable<OutGoingChatMessage> getOutGoingChatMessagesObservable();
}