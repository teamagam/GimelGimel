package com.teamagam.gimelgimel.domain.messages;

import io.reactivex.Observable;

public interface NetworkStateReceiverListener {
  void networkStateChange(Boolean isOnline);

  Observable<Boolean> getObservable();
}
