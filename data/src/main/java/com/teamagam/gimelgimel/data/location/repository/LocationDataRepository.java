package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import rx.Observable;
import rx.subjects.SerializedSubject;

@Singleton
public class LocationDataRepository implements LocationRepository, LocationEventFetcher {

  private final ConnectivityStatusRepository mGpsConnectivityStatusRepo;
  private final LocationFetcher mLocationFetcher;
  private final SerializedSubject<LocationSample, LocationSample> mSubject;
  private final GpsLocationListenerImpl mGpsLocationListener;
  private LocationSample mLastLocationSample;
  private LocationSample mLastServerSyncedLocationSample;

  private Boolean mIsFetching;

  @Inject
  public LocationDataRepository(
      @Named("gps")
          ConnectivityStatusRepository gpsConRepo, LocationFetcher locationFetcher) {
    mGpsConnectivityStatusRepo = gpsConRepo;
    mLocationFetcher = locationFetcher;
    mSubject = new SerializedSubjectBuilder().build();
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
  public Observable<LocationSample> getLocationObservable() {
    return mSubject.asObservable();
  }

  @Override
  public LocationSample getLastLocationSample() {
    return mLastLocationSample;
  }

  @Override
  public LocationSample getLastServerSyncedLocationSample() {
    return mLastServerSyncedLocationSample;
  }

  @Override
  public void setLastServerSyncedLocationSample(LocationSample locationSample) {
    mLastServerSyncedLocationSample = locationSample;
  }

  private class GpsLocationListenerImpl implements GpsLocationListener {

    @Override
    public void onNewLocation(LocationSample locationSample) {
      LocationDataRepository.this.mSubject.onNext(locationSample);
      mLastLocationSample = locationSample;
      mGpsConnectivityStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
    }

    @Override
    public void onBadConnection() {
      mGpsConnectivityStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    }
  }
}
