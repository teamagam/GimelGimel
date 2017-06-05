package com.teamagam.gimelgimel.data.alerts.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;

@Singleton
public class AlertsDataRepository implements AlertsRepository {

  private SubjectRepository<Alert> mAlertsRepo;

  @Inject
  public AlertsDataRepository() {
    mAlertsRepo = SubjectRepository.createReplayAll();
  }

  @Override
  public void addAlert(Alert alert) {
    mAlertsRepo.add(alert);
  }

  @Override
  public Observable<Alert> getAlertsObservable() {
    return mAlertsRepo.getObservable();
  }
}
