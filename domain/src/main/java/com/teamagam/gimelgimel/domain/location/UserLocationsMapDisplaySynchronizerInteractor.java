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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserLocationsMapDisplaySynchronizerInteractor extends BaseDataInteractor {

  private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private GeoEntitiesRepository mGeoEntitiesRepository;
  private UsersLocationRepository mUsersLocationRepository;
  private Map<String, UserEntity> mDisplayedUsernameToEntityMap;
  private Set<UserEntity> mCurrentlyDisplayed;

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
    mCurrentlyDisplayed = new HashSet<>();
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singleton(buildDisplayRequest(factory));
  }

  private DataSubscriptionRequest<?> buildDisplayRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(createIntervalObservable(),
        observable -> observable.doOnNext(tick -> hideAllUserLocations())
            .flatMapIterable(n -> mUsersLocationRepository.getLastLocations())
            .filter(ul -> !ul.isIrrelevant())
            .map(UserLocation::createUserEntity)
            .doOnNext(mGeoEntitiesRepository::add)
            .doOnNext(mDisplayedEntitiesRepository::show)
            .doOnNext(mCurrentlyDisplayed::add));
  }

  private void hideAllUserLocations() {
    for (UserEntity ue : mCurrentlyDisplayed) {
      mGeoEntitiesRepository.remove(ue.getId());
      mDisplayedEntitiesRepository.hide(ue);
    }
    mCurrentlyDisplayed.clear();
  }

  private Observable<Long> createIntervalObservable() {
    return Observable.interval(Constants.USERS_LOCATION_REFRESH_FREQUENCY_MS, TimeUnit.MILLISECONDS)
        .startWith((long) -1);
  }

  private void hideIfIrrelevant(UserLocation userLocation) {
    if (userLocation.isIrrelevant()) {
      UserEntity userEntity = mDisplayedUsernameToEntityMap.get(userLocation.getUser());
      mDisplayedEntitiesRepository.hide(userEntity);
      mDisplayedUsernameToEntityMap.remove(userLocation.getUser());
    }
  }
}