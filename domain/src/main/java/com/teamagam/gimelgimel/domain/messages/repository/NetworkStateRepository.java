package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import io.reactivex.Observable;

public interface NetworkStateRepository {

  Observable<ConnectivityStatus> getObservable();

  void startGettingNetworksStatesUpdates();
}