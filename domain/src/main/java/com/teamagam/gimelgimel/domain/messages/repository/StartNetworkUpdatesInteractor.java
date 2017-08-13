package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.NetworkStateReceiverListener;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Named;

public class StartNetworkUpdatesInteractor extends BaseSingleDataInteractor {

  private ConnectivityStatusRepository mNetworkStateRepository;
  private NetworkStateReceiverListener mNetworkStateReceiverListener;

  @Inject
  public StartNetworkUpdatesInteractor(ThreadExecutor threadExecutor,
      @Named("network") ConnectivityStatusRepository connectivityStatusRepository,
      NetworkStateReceiverListener networkStateReceiverListener) {
    super(threadExecutor);
    mNetworkStateRepository = connectivityStatusRepository;
    mNetworkStateReceiverListener = networkStateReceiverListener;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(Observable.just(mNetworkStateReceiverListener),
        receiverObservable -> receiverObservable.doOnNext(NetworkStateReceiverListener::start)
            .flatMap(NetworkStateReceiverListener::getObservable)
            .doOnNext(mNetworkStateRepository::setStatus));
  }
}