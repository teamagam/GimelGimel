package com.teamagam.gimelgimel.domain.base.interactors;

import com.google.common.io.Files;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;

import rx.Observable;

/**
 * Abstract interactor for displaying data objects on map.
 * This interactor adds the objects' entities to the displayed repository.
 * @param <T>
 */
public abstract class AbsDisplayDataOnMapInteractor<T extends GeoEntity> extends DoInteractor<T> {

    private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private GeoEntitiesRepository mGeoEntitiesRepository;

    protected AbsDisplayDataOnMapInteractor(ThreadExecutor threadExecutor,
                                            GeoEntitiesRepository geoEntitiesRepository,
                                            DisplayedEntitiesRepository displayedEntitiesRepository) {
        super(threadExecutor);
        mDisplayedEntitiesRepository = displayedEntitiesRepository;
        mGeoEntitiesRepository = geoEntitiesRepository;
    }

    @Override
    protected Observable<T> buildUseCaseObservable() {
        return getEntityObservable()
                .doOnNext(mGeoEntitiesRepository::add)
                .doOnNext(this::addSensorToDisplayedEntities);
    }

    protected abstract Observable<T> getEntityObservable();

    private void addSensorToDisplayedEntities(GeoEntity geoEntity) {
        if (!mDisplayedEntitiesRepository.isNotShown(geoEntity)) {
            mDisplayedEntitiesRepository.hide(geoEntity);
        }
        mDisplayedEntitiesRepository.show(geoEntity);
    }
}
