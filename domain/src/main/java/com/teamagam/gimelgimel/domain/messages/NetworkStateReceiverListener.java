package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import io.reactivex.Observable;

public interface NetworkStateReceiverListener {
  void networkStateChange(ConnectivityStatus isOffline);

  Observable<ConnectivityStatus> getObservable();

  void start();
}
