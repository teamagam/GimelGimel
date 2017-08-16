package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import com.teamagam.gimelgimel.domain.messages.MessageSender;
import com.teamagam.gimelgimel.domain.messages.OutGoingMessageQueueSender;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import javax.inject.Inject;

public class DataOutGoingMessageQueueSender implements OutGoingMessageQueueSender {

  private OutGoingMessagesQueue mOutGoingMessagesQueue;
  private MessageSender mMessageSender;
  private MessageNotifications mMessageNotifications;
  private SentMessagesObserver mChatMessageObserver;
  private RetryWithDelay mRetryStrategy;

  @Inject
  public DataOutGoingMessageQueueSender(MessageNotifications messageNotifications,
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
    mChatMessageObserver = new SentMessagesObserver();
    mOutGoingMessagesQueue.getOutGoingChatMessagesObservable().retryWhen(mRetryStrategy)
        .doOnNext(chatMessage -> mMessageNotifications.sending())
        .flatMap(mMessageSender::sendMessage)
        .subscribe(mChatMessageObserver);
  }

  @Override
  public void stop() {
    if (mChatMessageObserver != null) {
      mChatMessageObserver.dispose();
      mChatMessageObserver = null;
    }
  }

  private class SentMessagesObserver extends ErrorLoggingObserver<OutGoingChatMessage> {
    @Override
    public void onNext(OutGoingChatMessage outGoingChatMessage) {
      mMessageNotifications.success();
      mOutGoingMessagesQueue.removeTopMessage();
    }

    @Override
    public void onError(Throwable e) {
      super.onError(e);
      mMessageNotifications.error();
      moveTopOutGoingMessageToBottom();
      start();
    }

    private void moveTopOutGoingMessageToBottom() {
      OutGoingChatMessage outGoingChatMessage = mOutGoingMessagesQueue.getTopMessage();
      mOutGoingMessagesQueue.removeTopMessage();
      mOutGoingMessagesQueue.addMessage(outGoingChatMessage);
    }
  }
}