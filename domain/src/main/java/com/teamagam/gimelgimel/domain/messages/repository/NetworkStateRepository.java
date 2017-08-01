package com.teamagam.gimelgimel.domain.messages.repository;

public interface NetworkStateRepository {
  boolean doesHaveInternetConnetion();

  void setInternetConnetion(boolean internetConnetion);
}
