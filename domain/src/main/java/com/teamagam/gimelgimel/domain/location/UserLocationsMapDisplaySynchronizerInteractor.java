package com.teamagam.gimelgimel.domain.location;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserLocationsMapDisplaySynchronizerInteractor extends BaseDataInteractor {

  private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private GeoEntitiesRepository mGeoEntitiesRepository;
  private UsersLocationRepository mUsersLocationRepository;
  private Map<String, UserEntity> mDisplayedUsernameToEntityMap;

  @Inject
  protected UserLocationsMapDisplaySynchronizerInteractor(ThreadExecutor threadExecutor,
      DisplayedEntitiesRepository displayedEntitiesRepository,
      GeoEntitiesRepository geoEntitiesRepository,
      UsersLocationRepository usersLocationRepository) {
    super(threadExecutor);
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mGeoEntitiesRepository = geoEntitiesRepository;
    mUsersLocationRepository = usersLocationRepository;
    mDisplayedUsernameToEntityMap = new HashMap<>();
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singleton(buildDisplayRequest(factory));
  }

  private DataSubscriptionRequest<?> buildDisplayRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(createIntervalObservable(),
        observable -> observable.flatMapIterable(n -> mUsersLocationRepository.getLastLocations())
            .doOnNext(this::handleUserLocation));
  }

  private Observable<Long> createIntervalObservable() {
    return Observable.interval(Constants.USERS_LOCATION_REFRESH_FREQUENCY_MS, TimeUnit.MILLISECONDS)
        .startWith((long) -1);
  }

  private void handleUserLocation(UserLocation userLocation) {
    if (userLocation.isIrrelevant()) {
      if (isDisplayed(userLocation)) {
        hide(userLocation);
      }
      return;
    }

    if (!isDisplayed(userLocation)) {
      show(userLocation);
      return;
    }

    if (isUpdatingExisting(userLocation)) {
      hide(userLocation);
      show(userLocation);
    }
  }

  private boolean isDisplayed(UserLocation userLocation) {
    return mDisplayedUsernameToEntityMap.containsKey(userLocation.getUser());
  }

  private void hide(UserLocation userLocation) {
    UserEntity ue = mDisplayedUsernameToEntityMap.get(userLocation.getUser());
    mDisplayedEntitiesRepository.hide(ue);
    mDisplayedUsernameToEntityMap.remove(userLocation.getUser());
  }

  private boolean isUpdatingExisting(UserLocation userLocation) {
    UserEntity userEntity = mDisplayedUsernameToEntityMap.get(userLocation.getUser());
    return userEntity != null && !Objects.equals(userEntity.getId(),
        userLocation.createUserEntity().getId());
  }

  private void show(UserLocation userLocation) {
    UserEntity ue = userLocation.createUserEntity();
    mGeoEntitiesRepository.add(ue);
    mDisplayedEntitiesRepository.show(ue);
    mDisplayedUsernameToEntityMap.put(userLocation.getUser(), ue);
  }
}