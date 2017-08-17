package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import io.reactivex.Observable;

public class NetworkStatusRepository implements ConnectivityStatusRepository {

  private SubjectRepository<ConnectivityStatus> mInnerRepo;
  private ConnectivityStatus mCurrentConnectivityStatus;

  public NetworkStatusRepository() {
    mInnerRepo = SubjectRepository.createSimpleSubject();
  }

  @Override
  public void setStatus(ConnectivityStatus status) {
    if (mCurrentConnectivityStatus != status) {
      mCurrentConnectivityStatus = status;
      mInnerRepo.add(mCurrentConnectivityStatus);
    }
  }

  @Override
  public Observable<ConnectivityStatus> getObservable() {
    return mInnerRepo.getObservable();
  }
}
