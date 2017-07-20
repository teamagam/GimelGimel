package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import io.reactivex.Observable;
import java.util.LinkedList;
import java.util.Queue;

public class OutGoingMessagesDataQueue implements OutGoingMessagesQueue {
  private Queue<ChatMessage> mMessagesQueue;
  private SubjectRepository<ChatMessage> mChatMessageSubject;

  public OutGoingMessagesDataQueue() {
    mMessagesQueue = new LinkedList<>();
    mChatMessageSubject = SubjectRepository.createReplayAll();
  }

  @Override
  public void addMessage(ChatMessage chatMessage) {
    mMessagesQueue.add(chatMessage);
    mChatMessageSubject.add(mMessagesQueue.poll());
  }

  @Override
  public void getMessage() {
    mMessagesQueue.peek();
  }

  @Override
  public boolean isEmpty() {
    return mMessagesQueue.isEmpty();
  }

  @Override
  public void removeMessage() {
    mMessagesQueue.poll();
  }

  @Override
  public Observable<ChatMessage> getObservable() {
    mChatMessageSubject = SubjectRepository.createReplayAll();
    for (ChatMessage chatMessage : mMessagesQueue) {
      mChatMessageSubject.add(chatMessage);
    }
    return mChatMessageSubject.getObservable();
  }
}