package com.teamagam.gimelgimel.domain.notifications.cellular_network;

import rx.Observable;

public interface CellularNetworkTypeRepository {

  int NETWORK_CODE_3G = 1;
  int NETWORK_CODE_2G = 2;

  Observable<Integer> getChangesObservable();
}