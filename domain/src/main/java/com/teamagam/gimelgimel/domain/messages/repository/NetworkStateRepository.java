package com.teamagam.gimelgimel.domain.messages.repository;

import io.reactivex.Observable;

public interface NetworkStateRepository {

  Observable<Boolean> getObservable();

  void startGettingNetworksStatesUpdates();

  void stopGettingNetworkStatesUpdates();
}