package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.messages.MessageSender;
import com.teamagam.gimelgimel.domain.messages.OutGoingMessageQueueSender;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;

public class OutGoingMessageQueueSenderImpl implements OutGoingMessageQueueSender {

  private OutGoingMessagesQueue mOutGoingMessagesQueue;
  private MessageSender mMessageSender;
  private MessageNotifications mMessageNotifications;
  private RetryWithDelay mRetryStrategy; // ?
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
    startLicencingForMessages();
  }

  @Override
  public void stop() {
    mChatMessageObserver.dispose();
  }

  private void startLicencingForMessages() {
    mChatMessageObserver = new ResourceObserver<ChatMessage>() {
      @Override
      public void onNext(ChatMessage chatMessage) {
        mMessageNotifications.sending();

        Observer<ChatMessage> sendConformationMessage = new Observer<ChatMessage>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(ChatMessage chatMessage) {
            mMessageNotifications.success();
            mOutGoingMessagesQueue.removeMessage();
          }

          @Override
          public void onError(Throwable e) {
            mMessageNotifications.error();
          }

          @Override
          public void onComplete() {
          }
        };

        mMessageSender.sendMessage(chatMessage).subscribe(sendConformationMessage);
      }

      @Override
      public void onError(Throwable e) {
      }

      @Override
      public void onComplete() {
      }
    };
    mOutGoingMessagesQueue.getObservable().subscribe(mChatMessageObserver);
  }
}