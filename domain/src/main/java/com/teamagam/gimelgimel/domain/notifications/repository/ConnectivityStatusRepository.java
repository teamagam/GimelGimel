package com.teamagam.gimelgimel.domain.notifications.repository;

import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;

import rx.Observable;

public interface ConnectivityStatusRepository {

    void setStatus(ConnectivityStatus status);

    Observable<ConnectivityStatus> getObservable();
}
