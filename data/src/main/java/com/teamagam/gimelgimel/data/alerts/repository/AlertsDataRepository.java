package com.teamagam.gimelgimel.data.alerts.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class AlertsDataRepository implements AlertsRepository {

    private ReplayRepository<Alert> mReplayRepository;

    @Inject
    public AlertsDataRepository() {
        mReplayRepository = ReplayRepository.createReplayCount(1);
    }

    @Override
    public void addAlert(Alert alert) {
        mReplayRepository.add(alert);
    }

    @Override
    public Observable<Alert> getAlertsObservable() {
        return mReplayRepository.getObservable();
    }
}
