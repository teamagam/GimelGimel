package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.data.base.ConsistentStatusEventRaiser;
import com.teamagam.gimelgimel.data.base.repository.SingleReplayRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

import rx.Observable;

/**
 * This repository updates the underlying state connectivity value only if the value is persistent
 * for the given timeframe.
 * Connectivity status is initialized to be CONNECTED
 */
public class PersistentConnectivityStatusRepositoryImpl implements ConnectivityStatusRepository {

    private static final ConnectivityStatus DEFAULT_CONNECTIVITY_STATUS =
            ConnectivityStatus.CONNECTED;


    private SingleReplayRepository<ConnectivityStatus> mInnerRepo;
    private ConsistentStatusEventRaiser<ConnectivityStatus> mConsistentEventRaiser;


    public PersistentConnectivityStatusRepositoryImpl(long consistentTimeFrameMS) {
        mInnerRepo = new SingleReplayRepository<>();
        mConsistentEventRaiser = new ConsistentStatusEventRaiser<>(
                consistentTimeFrameMS,
                DEFAULT_CONNECTIVITY_STATUS,
                consistentStatus -> mInnerRepo.setValue(consistentStatus));
    }

    @Override
    public void setStatus(ConnectivityStatus status) {
        mConsistentEventRaiser.updateStatus(status);
    }

    @Override
    public Observable<ConnectivityStatus> getObservable() {
        return mInnerRepo.getObservable();
    }
}
