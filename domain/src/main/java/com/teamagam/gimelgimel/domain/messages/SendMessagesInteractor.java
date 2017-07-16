package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.OutGoingMessagesQueue;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import java.util.Collections;
import javax.inject.Inject;

public class SendMessagesInteractor extends BaseDataInteractor {

  private OutGoingMessagesQueue mOutGoingMessagesQueue;
  private MessageSender mMessageSender;
  private MessageNotifications mMessageNotifications;

  @Inject
  public SendMessagesInteractor(ThreadExecutor threadExecutor,
      MessageNotifications messageNotifications,
      MessageSender messageSender,
      OutGoingMessagesQueue outGoingMessagesQueue) {
    super(threadExecutor);
    mMessageNotifications = messageNotifications;
    mMessageSender = messageSender;
    mOutGoingMessagesQueue = outGoingMessagesQueue;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest subscriptionRequest =
        factory.create(mOutGoingMessagesQueue.getObservable(),
            objectObservable -> objectObservable.doOnNext(m -> mMessageNotifications.sending())
                .flatMap(mMessageSender::sendMessage)
                .doOnNext(m -> mMessageNotifications.success())
                .doOnError(m -> mMessageNotifications.error()));
    return Collections.singletonList(subscriptionRequest);
  }
}