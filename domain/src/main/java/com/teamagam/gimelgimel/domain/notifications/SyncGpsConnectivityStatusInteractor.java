package com.teamagam.gimelgimel.domain.notifications;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.notifications.entity.GpsConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.GpsConnectivityStatusRepository;

import rx.Observable;
import rx.Subscriber;

@AutoFactory
public class SyncGpsConnectivityStatusInteractor extends SyncInteractor<GpsConnectivityStatus> {

    private final GpsConnectivityStatusRepository mGpsStatusRepository;

    protected SyncGpsConnectivityStatusInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided GpsConnectivityStatusRepository gpsStatusRepository,
            Subscriber<GpsConnectivityStatus> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mGpsStatusRepository = gpsStatusRepository;
    }

    @Override
    protected Observable<GpsConnectivityStatus> buildUseCaseObservable() {
        return mGpsStatusRepository.getObservable();
    }
}
