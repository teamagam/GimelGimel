package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Named;

public class SendMessageOnAvailableNetworkInteractor extends BaseDataInteractor {

  private ConnectivityStatusRepository mConnectivityStatusRepository;
  private OutGoingMessageQueueSender mOutGoingMessageQueueSender;

  @Inject
  public SendMessageOnAvailableNetworkInteractor(ThreadExecutor threadExecutor,
      @Named("network") ConnectivityStatusRepository connectivityStatusRepository,
      OutGoingMessageQueueSender outGoingMessageQueueSender) {
    super(threadExecutor);
    mConnectivityStatusRepository = connectivityStatusRepository;
    mOutGoingMessageQueueSender = outGoingMessageQueueSender;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest subscriptionRequest =
        factory.create(mConnectivityStatusRepository.getObservable(),
            objectObservable -> objectObservable.doOnNext(this::handleMessagesByNetworkStatus));
    return Collections.singletonList(subscriptionRequest);
  }

  private void handleMessagesByNetworkStatus(ConnectivityStatus connectivityStatus) {
    if (connectivityStatus.isConnected()) {
      mOutGoingMessageQueueSender.start();
    } else {
      mOutGoingMessageQueueSender.stop();
    }
  }
}
