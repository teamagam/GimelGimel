package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.domain.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

import rx.Observable;
import rx.subjects.PublishSubject;

public class LocationRepositoryImpl implements LocationRepository, LocationFetcher {

    private GpsLocationProvider mLocationProvider;
    private PublishSubject<LocationSampleEntity> mSubject;
    private GpsLocationListenerImpl mGpsLocationListener;
    private Boolean mIsFetching;

    public LocationRepositoryImpl(GpsLocationProvider provider) {
        mLocationProvider = provider;
        mSubject = PublishSubject.create();
        mGpsLocationListener = new GpsLocationListenerImpl();
        mIsFetching = false;
    }

    @Override
    public void startFetching() {
        if(!mIsFetching) {
            mLocationProvider.addListener(mGpsLocationListener);
            mLocationProvider.start();

            mIsFetching = true;
        }
    }

    @Override
    public void stopFetching() {
        if(mIsFetching) {
            mLocationProvider.removeListener(mGpsLocationListener);
            mLocationProvider.stop();

            mIsFetching = false;
        }
    }

    @Override
    public Observable<LocationSampleEntity> getLocationObservable() {
        return mSubject;
    }

    @Override
    public LocationSampleEntity getLastLocationSample() {
        return mLocationProvider.getLastLocationSample();
    }

    private class GpsLocationListenerImpl implements GpsLocationListener {

        @Override
        public void onNewLocation(LocationSampleEntity locationSampleEntity) {
            LocationRepositoryImpl.this.mSubject.onNext(locationSampleEntity);
        }
    }
}
