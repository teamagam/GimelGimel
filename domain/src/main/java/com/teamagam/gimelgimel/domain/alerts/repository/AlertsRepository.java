package com.teamagam.gimelgimel.domain.alerts.repository;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;

import rx.Observable;

public interface AlertsRepository {

    void addAlert(Alert alert);

    Observable<Alert> getAlertsObservable();

}
