package com.teamagam.gimelgimel.domain.messages.entity;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.util.LinkedList;
import java.util.Queue;

public class OutGoingMessagesQueue {
  private Queue<ChatMessage> mMessagesQueue;
  private Subject<ChatMessage> mChatMessageSubject;

  public OutGoingMessagesQueue() {
    mMessagesQueue = new LinkedList<>();
    mChatMessageSubject = PublishSubject.create();
  }

  public void addMessage(ChatMessage chatMessage) {
    mMessagesQueue.add(chatMessage);
    mChatMessageSubject.onNext(mMessagesQueue.poll());
  }

  public void getMessage() {
    mMessagesQueue.peek();
  }

  public boolean isEmpty() {
    return mMessagesQueue.isEmpty();
  }

  public Subject<ChatMessage> getObservable() {
    return mChatMessageSubject;
  }
}