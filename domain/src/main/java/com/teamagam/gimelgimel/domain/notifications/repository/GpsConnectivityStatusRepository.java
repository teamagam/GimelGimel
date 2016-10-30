package com.teamagam.gimelgimel.domain.notifications.repository;

import com.teamagam.gimelgimel.domain.notifications.entity.GpsConnectivityStatus;

import rx.Observable;

public interface GpsConnectivityStatusRepository {

    void setStatus(GpsConnectivityStatus status);

    Observable<GpsConnectivityStatus> getObservable();
}
