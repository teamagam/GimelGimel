package com.teamagam.gimelgimel.domain.location;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;

/**
 * Created on 11/23/2016.
 * synchronize the users' location repository with displayed entities repository.
 * also updates the activity of the users with time.
 */
@Singleton
public class DisplayUsersLocationInteractor extends BaseDataInteractor {

  private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private GeoEntitiesRepository mGeoEntitiesRepository;
  private UsersLocationRepository mUsersLocationRepository;

  @Inject
  protected DisplayUsersLocationInteractor(ThreadExecutor threadExecutor,
      DisplayedEntitiesRepository displayedEntitiesRepository,
      GeoEntitiesRepository geoEntitiesRepository,
      UsersLocationRepository usersLocationRepository) {
    super(threadExecutor);
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mGeoEntitiesRepository = geoEntitiesRepository;
    mUsersLocationRepository = usersLocationRepository;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return Collections.singleton(buildDisplayRequest(factory));
  }

  private DataSubscriptionRequest<?> buildDisplayRequest(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    return factory.create(createIntervalObservable(),
        observable -> observable.flatMapIterable(n -> mUsersLocationRepository.getLastLocations())
            .doOnNext(this::hideOldUserLocations)
            .filter((ul) -> !isOld(ul))
            .map(this::createUserEntity)
            .doOnNext(mGeoEntitiesRepository::update)
            .filter(ue -> !mDisplayedEntitiesRepository.isShown(ue))
            .doOnNext(mDisplayedEntitiesRepository::show));
  }

  private Observable<Long> createIntervalObservable() {
    return Observable.interval(Constants.USERS_LOCATION_REFRESH_FREQUENCY_MS, TimeUnit.MILLISECONDS)
        .startWith((long) -1);
  }

  private void hideOldUserLocations(UserLocation userLocation) {
    if (isOld(userLocation)) {
      mDisplayedEntitiesRepository.hide(createUserEntity(userLocation));
    }
  }

  private Boolean isOld(UserLocation ul) {
    return ul.getLocationSample().getAgeMillis() >= Constants.USER_LOCATION_RELEVANCE_THRESHOLD_MS;
  }

  private UserEntity createUserEntity(UserLocation userLocation) {
    UserSymbol symbol = createUserSymbol(userLocation);
    return new UserEntity(userLocation.getUser(), userLocation.getUser(),
        userLocation.getLocationSample().getLocation(), symbol);
  }

  private UserSymbol createUserSymbol(UserLocation userLocation) {
    if (isActiveUser(userLocation.getLocationSample().getAgeMillis())) {
      return UserSymbol.createActive(userLocation.getUser(), false);
    } else {
      return UserSymbol.createStale(userLocation.getUser(), false);
    }
  }

  private boolean isActiveUser(long ageMillis) {
    return ageMillis < Constants.USER_LOCATION_STALE_THRESHOLD_MS;
  }
}