package com.teamagam.gimelgimel.data.user.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.data.message.repository.DataOutGoingMessageQueueSender;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.messages.NetworkStateReceiverListener;
import com.teamagam.gimelgimel.domain.messages.repository.NetworkStateRepository;
import io.reactivex.Observable;
import io.reactivex.observers.ResourceObserver;
import javax.inject.Inject;

public class DataNetworkStateRepository implements NetworkStateRepository {

  public static final int REPLAY_COUNT = 1;
  private static final Logger sLogger =
      LoggerFactory.create(DataOutGoingMessageQueueSender.class.getSimpleName());
  private NetworkStateUpdatesObserver mNetworkStateUpdatesObserver;
  private SubjectRepository<Boolean> mNetworkStateSubject;
  private NetworkStateReceiverListener mNetworkStateReceiverListener;

  @Inject
  public DataNetworkStateRepository(NetworkStateReceiverListener networkStateReceiverListener) {
    mNetworkStateSubject = SubjectRepository.createReplayCount(REPLAY_COUNT);
    mNetworkStateReceiverListener = networkStateReceiverListener;
  }

  @Override
  public Observable<Boolean> getObservable() {
    return mNetworkStateSubject.getObservable();
  }

  @Override
  public void startGettingNetworksStatesUpdates() {
    mNetworkStateUpdatesObserver = new NetworkStateUpdatesObserver();
    mNetworkStateReceiverListener.getObservable().subscribe(mNetworkStateUpdatesObserver);
    mNetworkStateReceiverListener.start();
  }

  @Override
  public void stopGettingNetworkStatesUpdates() {
    mNetworkStateUpdatesObserver.dispose();
    mNetworkStateUpdatesObserver = null;
  }

  private class NetworkStateUpdatesObserver extends ResourceObserver<Boolean> {
    @Override
    public void onNext(Boolean isOnline) {
      mNetworkStateSubject.add(isOnline);
    }

    @Override
    public void onError(Throwable e) {
      sLogger.d("On Error", e);
    }

    @Override
    public void onComplete() {
    }
  }
}