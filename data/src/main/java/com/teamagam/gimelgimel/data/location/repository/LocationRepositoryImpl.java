package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.subjects.PublishSubject;

public class LocationRepositoryImpl implements LocationRepository, LocationEventFetcher {

    private final ConnectivityStatusRepository mGpsConnectivityStatusRepo;
    private final LocationFetcher mLocationFetcher;
    private final PublishSubject<LocationSampleEntity> mSubject;
    private final GpsLocationListenerImpl mGpsLocationListener;

    private Boolean mIsFetching;

    @Inject
    public LocationRepositoryImpl(@Named("gps") ConnectivityStatusRepository gpsConRepo,
                                  LocationFetcher locationFetcher) {
        mGpsConnectivityStatusRepo = gpsConRepo;
        mLocationFetcher = locationFetcher;
        mSubject = PublishSubject.create();
        mGpsLocationListener = new GpsLocationListenerImpl();
        mIsFetching = false;
    }

    @Override
    public void startFetching() {
        if (!mIsFetching) {
            mLocationFetcher.addListener(mGpsLocationListener);
            mLocationFetcher.start();

            mIsFetching = true;
        }
    }

    @Override
    public void stopFetching() {
        if (mIsFetching) {
            mLocationFetcher.removeListener(mGpsLocationListener);
            mLocationFetcher.stop();

            mIsFetching = false;
        }
    }

    @Override
    public Observable<LocationSampleEntity> getLocationObservable() {
        return mSubject;
    }

    @Override
    public LocationSampleEntity getLastLocationSample() {
        return mLocationFetcher.getLastLocationSample();
    }

    private class GpsLocationListenerImpl implements GpsLocationListener {

        @Override
        public void onNewLocation(LocationSampleEntity locationSampleEntity) {
            LocationRepositoryImpl.this.mSubject.onNext(locationSampleEntity);

            mGpsConnectivityStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
        }

        @Override
        public void onBadConnection() {
            mGpsConnectivityStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
        }
    }
}
