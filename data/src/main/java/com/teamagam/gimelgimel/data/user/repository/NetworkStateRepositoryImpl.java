package com.teamagam.gimelgimel.data.user.repository;

import com.teamagam.gimelgimel.domain.messages.repository.NetworkStateRepository;

public class NetworkStateRepositoryImpl implements NetworkStateRepository {

  private Boolean mNetworkState;

  public NetworkStateRepositoryImpl() {
    mNetworkState = false;
  }

  @Override
  public boolean doesHaveInternetConnetion() {
    return mNetworkState;
  }

  @Override
  public void setInternetConnetion(boolean networkStates) {
    mNetworkState = networkStates;
  }
}