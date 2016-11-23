package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.MyLocationEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

import javax.inject.Inject;

import rx.Observable;

public class DisplayMyLocationOnMapInteractor extends DoInteractor<LocationSample> {


    private static final String MY_LOCATION_GEO_ENTITY_ID = "my_location_geo_entity_id";
    private final LocationRepository mLocationRepository;
    private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;

    private GeoEntity mCurrentLocationGeoEntity;

    @Inject
    public DisplayMyLocationOnMapInteractor(
            ThreadExecutor threadExecutor,
            LocationRepository mLocationRepository,
            DisplayedEntitiesRepository mDisplayedEntitiesRepository) {
        super(threadExecutor);
        this.mLocationRepository = mLocationRepository;
        this.mDisplayedEntitiesRepository = mDisplayedEntitiesRepository;
    }

    @Override
    protected Observable<LocationSample> buildUseCaseObservable() {
        return mLocationRepository.getLocationObservable()
                .doOnNext(ls -> displayMyLocation(ls));
    }


    private synchronized void displayMyLocation(LocationSample locationSample) {
        removePreviousLocation();
        displayNewLocation(locationSample);
    }

    private void displayNewLocation(LocationSample locationSample) {
        mCurrentLocationGeoEntity = buildMyLocationGeoEntity(locationSample);
        mDisplayedEntitiesRepository.show(mCurrentLocationGeoEntity);
    }

    private GeoEntity buildMyLocationGeoEntity(LocationSample locationSample) {
        return new MyLocationEntity(MY_LOCATION_GEO_ENTITY_ID, "Me", new MyLocationSymbol(true),
                locationSample.getLocation());
    }

    private void removePreviousLocation() {
        if (mCurrentLocationGeoEntity != null) {
            mDisplayedEntitiesRepository.hide(mCurrentLocationGeoEntity);
            mCurrentLocationGeoEntity = null;
        }
    }
}
