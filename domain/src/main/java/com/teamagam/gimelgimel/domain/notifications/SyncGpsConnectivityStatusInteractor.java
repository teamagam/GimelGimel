package com.teamagam.gimelgimel.domain.notifications;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;

@AutoFactory
public class SyncGpsConnectivityStatusInteractor extends SyncInteractor<ConnectivityStatus> {

    private final ConnectivityStatusRepository mGpsStatusRepository;

    protected SyncGpsConnectivityStatusInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided @Named("gps") ConnectivityStatusRepository gpsStatusRepository,
            Subscriber<ConnectivityStatus> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mGpsStatusRepository = gpsStatusRepository;
    }

    @Override
    protected Observable<ConnectivityStatus> buildUseCaseObservable() {
        return mGpsStatusRepository.getObservable();
    }
}
