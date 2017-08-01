package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.messages.MessageSender;
import com.teamagam.gimelgimel.domain.messages.OutGoingMessageQueueSender;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import io.reactivex.observers.ResourceObserver;

public class OutGoingMessageQueueSenderImpl implements OutGoingMessageQueueSender {

  private OutGoingMessagesQueue mOutGoingMessagesQueue;
  private MessageSender mMessageSender;
  private MessageNotifications mMessageNotifications;
  private RetryWithDelay mRetryStrategy;

  private ResourceObserver<ChatMessage> mChatMessageObserver;

  public OutGoingMessageQueueSenderImpl(MessageNotifications messageNotifications,
      MessageSender messageSender,
      OutGoingMessagesQueue outGoingMessagesQueue,
      RetryWithDelay retryStrategy) {
    mMessageNotifications = messageNotifications;
    mMessageSender = messageSender;
    mOutGoingMessagesQueue = outGoingMessagesQueue;
    mRetryStrategy = retryStrategy;
  }

  @Override
  public void start() {
    startGettingMessages();
    mOutGoingMessagesQueue.addAllMessagesToObservable();
  }

  @Override
  public void stop() {
    mChatMessageObserver.onComplete();
    mChatMessageObserver = null;
  }

  private void startGettingMessages() {
    mChatMessageObserver = new SentMessagesObserver();
    mOutGoingMessagesQueue.getObservable()
        .doOnNext(chatMessage -> mMessageNotifications.sending())
        .flatMap(mMessageSender::sendMessage)
        .subscribe(mChatMessageObserver);
  }

  private class SentMessagesObserver extends ResourceObserver<ChatMessage> {
    @Override
    public void onNext(ChatMessage chatMessage) {
      mMessageNotifications.success();
      mOutGoingMessagesQueue.removeMessage();
    }

    @Override
    public void onError(Throwable e) {
      e.printStackTrace();
      mMessageNotifications.error();
    }

    @Override
    public void onComplete() {
      dispose();
    }
  }
}