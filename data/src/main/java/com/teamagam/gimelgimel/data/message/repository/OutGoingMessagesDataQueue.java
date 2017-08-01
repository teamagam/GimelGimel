package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import io.reactivex.Observable;
import java.util.LinkedList;
import java.util.Queue;

public class OutGoingMessagesDataQueue implements OutGoingMessagesQueue {
  public static final int MESSAGE_SEND_DELAY_AMOUNT = 2000;
  private Queue<ChatMessage> mMessagesQueue;
  private SubjectRepository<ChatMessage> mChatMessageSubject;

  public OutGoingMessagesDataQueue() {
    mMessagesQueue = new LinkedList<>();
    mChatMessageSubject = SubjectRepository.createSimpleSubject();
  }

  @Override
  public synchronized void addMessage(ChatMessage chatMessage) {
    mMessagesQueue.add(chatMessage);
    mChatMessageSubject.add(mMessagesQueue.peek());
  }

  @Override
  public ChatMessage getMessage() {
    return mMessagesQueue.peek();
  }

  @Override
  public boolean isEmpty() {
    return mMessagesQueue.isEmpty();
  }

  @Override
  public synchronized void removeMessage() {
    mMessagesQueue.poll();
  }

  @Override
  public synchronized Observable<ChatMessage> getObservable() {
    return mChatMessageSubject.getObservable();
  }

  public void addAllMessagesToObservable() {
    Queue<ChatMessage> chatMessagesQueueCopy = new LinkedList<>(mMessagesQueue);
    for (ChatMessage chatMessage : chatMessagesQueueCopy) {
      delaySending();
      mChatMessageSubject.add(chatMessage);
    }
  }

  private void delaySending() {
    try {
      Thread.sleep(MESSAGE_SEND_DELAY_AMOUNT);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}