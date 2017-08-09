package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.messages.MessageSender;
import com.teamagam.gimelgimel.domain.messages.OutGoingMessageQueueSender;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import io.reactivex.observers.ResourceObserver;
import javax.inject.Inject;

public class DataOutGoingMessageQueueSender implements OutGoingMessageQueueSender {

  private static final Logger sLogger =
      LoggerFactory.create(DataOutGoingMessageQueueSender.class.getSimpleName());

  private OutGoingMessagesQueue mOutGoingMessagesQueue;
  private MessageSender mMessageSender;
  private MessageNotifications mMessageNotifications;
  private SentMessagesObserver mChatMessageObserver;

  @Inject
  public DataOutGoingMessageQueueSender(MessageNotifications messageNotifications,
      MessageSender messageSender,
      OutGoingMessagesQueue outGoingMessagesQueue) {
    mMessageNotifications = messageNotifications;
    mMessageSender = messageSender;
    mOutGoingMessagesQueue = outGoingMessagesQueue;
  }

  @Override
  public void start() {
    mChatMessageObserver = new SentMessagesObserver();
    mOutGoingMessagesQueue.getObservable()
        .doOnNext(chatMessage -> mMessageNotifications.sending())
        .flatMap(mMessageSender::sendMessage)
        .subscribe(mChatMessageObserver);
  }

  @Override
  public void stop() {
    mChatMessageObserver.dispose();
    mChatMessageObserver = null;
  }

  private class SentMessagesObserver extends ResourceObserver<OutGoingChatMessage> {
    @Override
    public void onNext(OutGoingChatMessage chatMessage) {
      mMessageNotifications.success();
      mOutGoingMessagesQueue.removeTopMessage();
    }

    @Override
    public void onError(Throwable e) {
      sLogger.d("On Error", e);
      mMessageNotifications.error();
      moveTopOutGoingMessageToBottom();
    }

    @Override
    public void onComplete() {
    }

    private void moveTopOutGoingMessageToBottom() {
      OutGoingChatMessage outGoingChatMessage = mOutGoingMessagesQueue.getTopMessage();
      mOutGoingMessagesQueue.removeTopMessage();
      mOutGoingMessagesQueue.addMessage(outGoingChatMessage);
    }
  }
}