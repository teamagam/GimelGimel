package com.teamagam.gimelgimel.domain.location;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;

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
public class DisplayUsersLocationInteractor extends DoInteractor {

    private static final String LAYER_ID = "UsersLayer";

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
    protected Observable<UserEntity> buildUseCaseObservable() {
        return createIntervalObservable()
                .startWith((long) -1)
                .flatMap(n -> mUsersLocationRepository.getLastLocations())
                .mergeWith(mUsersLocationRepository.getUsersLocationUpdates())
                .map(this::createUserEntity)
                .doOnNext(mGeoEntitiesRepository::update)
                .filter(ue->!mDisplayedEntitiesRepository.isShown(ue))
                .doOnNext(mDisplayedEntitiesRepository::show);
    }

    private Observable<Long> createIntervalObservable() {
        return Observable.interval(Constants.USERS_LOCATION_REFRESH_FREQUENCY_MS,
                TimeUnit.MILLISECONDS);
    }

    private UserEntity createUserEntity(UserLocation userLocation) {
        UserSymbol symbol = createUserSymbol(userLocation);
        UserEntity userEntity = new UserEntity(userLocation.getUser(),
                userLocation.getUser(), userLocation.getLocationSample().getLocation(),
                symbol);
        userEntity.setLayerTag(LAYER_ID);
        return userEntity;
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