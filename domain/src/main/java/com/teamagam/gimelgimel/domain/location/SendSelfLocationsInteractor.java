package com.teamagam.gimelgimel.domain.location;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.util.Collections;
import javax.inject.Inject;

public class SendSelfLocationsInteractor extends BaseDataInteractor {

  private UserPreferencesRepository mUserPreferences;
  private SpatialEngine mSpatialEngine;
  private LocationRepository mLocationRepository;

  @Inject
  public SendSelfLocationsInteractor(ThreadExecutor threadExecutor,
      UserPreferencesRepository userPreferences,
      SpatialEngine spatialEngine,
      LocationRepository locationRepository) {
    super(threadExecutor);
    mUserPreferences = userPreferences;
    mSpatialEngine = spatialEngine;
    mLocationRepository = locationRepository;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singletonList(buildSendRequest(factory));
  }

  private DataSubscriptionRequest buildSendRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(mLocationRepository.getLocationObservable(),
        locationSampleObservable -> locationSampleObservable.filter(this::shouldUpdateServer)
            .map(this::createUserLocation)
            .flatMap(mLocationRepository::sendUserLocation)
            .doOnNext(this::updateLastSyncedLocation));
  }

  private boolean shouldUpdateServer(LocationSample locationSample) {
    LocationSample serverLocation = mLocationRepository.getLastServerSyncedLocationSample();
    return serverLocation == null || isNewEnough(locationSample, serverLocation) || isFarEnough(
        locationSample, serverLocation);
  }

  private boolean isFarEnough(LocationSample locationSample, LocationSample serverLocation) {
    return mSpatialEngine.distanceInMeters(locationSample.getLocation(),
        serverLocation.getLocation())
        > Constants.LOCATION_DISTANCE_CHANGE_SERVER_UPDATE_THRESHOLD_METERS;
  }

  private boolean isNewEnough(LocationSample locationSample, LocationSample serverLocation) {
    return locationSample.getTime() - serverLocation.getTime()
        > Constants.LOCATION_TIME_CHANGE_SERVER_UPDATE_THRESHOLD_MS;
  }

  private UserLocation createUserLocation(LocationSample locationSample) {
    return new UserLocation(getSenderId(), locationSample);
  }

  private String getSenderId() {
    return mUserPreferences.getString(Constants.USERNAME_PREFERENCE_KEY);
  }

  private void updateLastSyncedLocation(UserLocation userLocation) {
    mLocationRepository.setLastServerSyncedLocationSample(userLocation.getLocationSample());
  }
}
