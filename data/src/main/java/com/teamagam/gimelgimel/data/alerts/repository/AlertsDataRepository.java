package com.teamagam.gimelgimel.data.alerts.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlertsDataRepository implements AlertsRepository {

  private SubjectRepository<Alert> mAlerts;

  @Inject
  public AlertsDataRepository() {
    mAlerts = SubjectRepository.createReplayAll();
  }

  @Override
  public void addAlert(Alert alert) {
    mAlerts.add(alert);
  }

  @Override
  public Observable<Alert> getAlertsObservable() {
    return mAlerts.getObservable();
  }
}
