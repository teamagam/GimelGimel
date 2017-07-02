package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.data.response.adapters.ServerDataMapper;
import com.teamagam.gimelgimel.data.response.entity.UserLocationResponse;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import io.reactivex.Observable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class LocationDataRepository implements LocationRepository, LocationEventFetcher {

  private final ConnectivityStatusRepository mGpsConnectivityStatusRepo;
  private final LocationFetcher mLocationFetcher;
  private final SubjectRepository<LocationSample> mSubjectRepository;
  private final GpsLocationListenerImpl mGpsLocationListener;
  private final ServerDataMapper mDataMapper;
  private final CloudMessagesSource mSource;
  private LocationSample mLastLocationSample;
  private LocationSample mLastServerSyncedLocationSample;

  private Boolean mIsFetching;

  @Inject
  public LocationDataRepository(@Named("gps") ConnectivityStatusRepository gpsConRepo,
      LocationFetcher locationFetcher,
      ServerDataMapper dataMapper,
      CloudMessagesSource source) {
    mGpsConnectivityStatusRepo = gpsConRepo;
    mLocationFetcher = locationFetcher;
    mSubjectRepository = SubjectRepository.createSimpleSubject();
    mGpsLocationListener = new GpsLocationListenerImpl();
    mDataMapper = dataMapper;
    mSource = source;
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
    return mSubjectRepository.getObservable();
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

  @Override
  public Observable<UserLocation> sendUserLocation(UserLocation userLocation) {
    return mSource.sendMessage(mDataMapper.transformToData(userLocation))
        .cast(UserLocationResponse.class)
        .map(mDataMapper::transform);
  }

  private class GpsLocationListenerImpl implements GpsLocationListener {

    @Override
    public void onNewLocation(LocationSample locationSample) {
      LocationDataRepository.this.mSubjectRepository.add(locationSample);
      mLastLocationSample = locationSample;
      mGpsConnectivityStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
    }

    @Override
    public void onBadConnection() {
      mGpsConnectivityStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    }
  }
}
