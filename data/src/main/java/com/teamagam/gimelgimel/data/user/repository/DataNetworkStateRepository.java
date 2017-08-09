package com.teamagam.gimelgimel.data.user.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import com.teamagam.gimelgimel.domain.messages.NetworkStateReceiverListener;
import com.teamagam.gimelgimel.domain.messages.repository.NetworkStateRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import io.reactivex.Observable;
import javax.inject.Inject;

public class DataNetworkStateRepository implements NetworkStateRepository {

  public static final int REPLAY_COUNT = 1;

  //// TODO: 09/08/2017 : All your DataNetworkStateRepository can reuse the existing ConnectivityStatusRepository.

  private NetworkStateUpdatesObserver mNetworkStateUpdatesObserver;
  private SubjectRepository<ConnectivityStatus> mNetworkStateSubject;
  private NetworkStateReceiverListener mNetworkStateReceiverListener;

  @Inject
  public DataNetworkStateRepository(NetworkStateReceiverListener networkStateReceiverListener) {
    mNetworkStateSubject = SubjectRepository.createReplayCount(REPLAY_COUNT);
    mNetworkStateReceiverListener = networkStateReceiverListener;
  }

  @Override
  public Observable<ConnectivityStatus> getObservable() {
    return mNetworkStateSubject.getObservable();
  }

  @Override
  public void startGettingNetworksStatesUpdates() {
    mNetworkStateUpdatesObserver = new NetworkStateUpdatesObserver();
    mNetworkStateReceiverListener.getObservable().subscribe(mNetworkStateUpdatesObserver);
    mNetworkStateReceiverListener.start();
  }

  private class NetworkStateUpdatesObserver extends ErrorLoggingObserver<ConnectivityStatus> {
    @Override
    public void onNext(ConnectivityStatus isOnline) {
      mNetworkStateSubject.add(isOnline);
    }
  }
}