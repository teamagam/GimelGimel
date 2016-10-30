package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.data.base.ConsistentStatusEventRaiser;
import com.teamagam.gimelgimel.data.base.repository.SingleValueRepository;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.notifications.entity.GpsConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.GpsConnectivityStatusRepository;

import rx.Observable;

public class GpsConnectivityStatusRepositoryImpl implements GpsConnectivityStatusRepository {

    private static final GpsConnectivityStatus DEFAULT_GPS_STATUS =
            GpsConnectivityStatus.createConnected();


    private SingleValueRepository<GpsConnectivityStatus> mInnerRepo;
    private ConsistentStatusEventRaiser<GpsConnectivityStatus> mConsistentEventRaiser;


    public GpsConnectivityStatusRepositoryImpl() {
        mInnerRepo = new SingleValueRepository<>();
        mConsistentEventRaiser = new ConsistentStatusEventRaiser<>(
                Constants.GPS_STATUS_CONSISTENT_TIMEFRAME_MS,
                DEFAULT_GPS_STATUS,
                consistentStatus -> mInnerRepo.setValue(consistentStatus));
    }

    @Override
    public void setStatus(GpsConnectivityStatus status) {
        mConsistentEventRaiser.updateStatus(status);
    }

    @Override
    public Observable<GpsConnectivityStatus> getObservable() {
        return mInnerRepo.getObservable();
    }
}
