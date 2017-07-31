package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.repository.NetworkStateRepository;
import java.util.Collections;
import javax.inject.Inject;

public class SendMessageOnAvailableNetworkInteractor extends BaseDataInteractor {

  private NetworkStateRepository mNetworkStateRepository;
  private NetworkStateReceiverListener mNetworkStateReceiverListener;
  private OutGoingMessageQueueSender mOutGoingMessageQueueSender;

  @Inject
  public SendMessageOnAvailableNetworkInteractor(ThreadExecutor threadExecutor,
      NetworkStateRepository networkStateRepository,
      NetworkStateReceiverListener networkStateReceiverListener,
      OutGoingMessageQueueSender outGoingMessageQueueSender) {
    super(threadExecutor);
    mNetworkStateRepository = networkStateRepository;
    mNetworkStateReceiverListener = networkStateReceiverListener;
    mOutGoingMessageQueueSender = outGoingMessageQueueSender;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest subscriptionRequest =
        factory.create(mNetworkStateReceiverListener.getObservable(),
            objectObservable -> objectObservable.doOnNext(
                mNetworkStateRepository::setInternetConnetion)
                .doOnNext(this::handleMessagesByNetworkStatus));
    return Collections.singletonList(subscriptionRequest);
  }

  private void handleMessagesByNetworkStatus(Boolean isOnline) {
    if (isOnline) {
      mOutGoingMessageQueueSender.start();
    } else {
      mOutGoingMessageQueueSender.stop();
    }
  }
}
