package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import rx.Observable;

/**
 * Abstract interactor for displaying data objects on map.
 * This interactor adds the objects' entities to the displayed repository.
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
    return getEntityObservable().doOnNext(mGeoEntitiesRepository::add)
        .doOnNext(this::displayGeoEntity);
  }

  protected abstract Observable<T> getEntityObservable();

  private void displayGeoEntity(GeoEntity geoEntity) {
    if (mDisplayedEntitiesRepository.isShown(geoEntity)) {
      mDisplayedEntitiesRepository.hide(geoEntity);
    }
    mDisplayedEntitiesRepository.show(geoEntity);
  }
}
