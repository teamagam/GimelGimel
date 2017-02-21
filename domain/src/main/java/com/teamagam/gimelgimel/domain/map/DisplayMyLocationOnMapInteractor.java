package com.teamagam.gimelgimel.domain.map;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.MyLocationEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

import javax.inject.Inject;

import rx.Observable;

public class DisplayMyLocationOnMapInteractor extends DoInteractor<LocationSample> {

    private static final String MY_LOCATION_GEO_ENTITY_ID = "my_location_geo_entity_id";
    private static final String LAYER_ID = "my_location_layer";
    private final LocationRepository mLocationRepository;
    private GeoEntitiesRepository mGeoEntitiesRepository;
    private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;

    @Inject
    public DisplayMyLocationOnMapInteractor(
            ThreadExecutor threadExecutor,
            LocationRepository mLocationRepository,
            GeoEntitiesRepository geoEntitiesRepository,
            DisplayedEntitiesRepository mDisplayedEntitiesRepository) {
        super(threadExecutor);
        this.mLocationRepository = mLocationRepository;
        mGeoEntitiesRepository = geoEntitiesRepository;
        this.mDisplayedEntitiesRepository = mDisplayedEntitiesRepository;
    }

    @Override
    protected Observable<LocationSample> buildUseCaseObservable() {
        return mLocationRepository.getLocationObservable()
                .doOnNext(this::displayMyLocation);
    }


    private synchronized void displayMyLocation(LocationSample locationSample) {
        displayNewLocation(locationSample);
    }

    private void displayNewLocation(LocationSample locationSample) {
        GeoEntity currentLocationGeoEntity = buildMyLocationGeoEntity(locationSample);
        mGeoEntitiesRepository.update(currentLocationGeoEntity);
        if (!mDisplayedEntitiesRepository.isShown(currentLocationGeoEntity)) {
            mDisplayedEntitiesRepository.show(currentLocationGeoEntity);
        }
    }

    private GeoEntity buildMyLocationGeoEntity(LocationSample locationSample) {
        MyLocationEntity entity = new MyLocationEntity(MY_LOCATION_GEO_ENTITY_ID, "Me",
                new MyLocationSymbol(false),
                locationSample.getLocation());
        entity.setLayerTag(LAYER_ID);
        return entity;
    }
}