package com.teamagam.gimelgimel.data.user.repository;

import com.teamagam.gimelgimel.domain.messages.repository.NetworkStateRepository;

public class NetworkStateRepositoryImpl implements NetworkStateRepository {

  private boolean mNetworkStates;

  public NetworkStateRepositoryImpl() {
    mNetworkStates = false;
  }

  @Override
  public boolean doesHaveInternetConnetion() {
    return mNetworkStates;
  }

  @Override
  public void setInternetConnetion(boolean networkStates) {
    mNetworkStates = networkStates;
  }
}