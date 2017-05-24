package com.teamagam.gimelgimel.domain.notifications.cellular_network;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

public class Update3GConnectivityStatusInteractor extends BaseDataInteractor {
  private final ConnectivityStatusRepository m3GConnectivityStatusRepository;
  private final CellularNetworkTypeRepository mCellularNetworkTypeRepository;

  @Inject
  public Update3GConnectivityStatusInteractor(ThreadExecutor threadExecutor,
      @Named("3g")
          ConnectivityStatusRepository threeGConnectivityStatusRepository,
      CellularNetworkTypeRepository cellularNetworkTypeRepository) {
    super(threadExecutor);
    m3GConnectivityStatusRepository = threeGConnectivityStatusRepository;
    mCellularNetworkTypeRepository = cellularNetworkTypeRepository;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
      DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(factory.create(Observable.just(mCellularNetworkTypeRepository),
        repoObservable -> repoObservable.flatMap(
            CellularNetworkTypeRepository::getChangesObservable)
            .doOnNext(this::updateConnectivityRepository)));
  }

  private void updateConnectivityRepository(Integer changeEvent) {
    if (changeEvent == CellularNetworkTypeRepository.NETWORK_CODE_2G) {
      m3GConnectivityStatusRepository.setStatus(ConnectivityStatus.DISCONNECTED);
    } else {
      m3GConnectivityStatusRepository.setStatus(ConnectivityStatus.CONNECTED);
    }
  }
}
