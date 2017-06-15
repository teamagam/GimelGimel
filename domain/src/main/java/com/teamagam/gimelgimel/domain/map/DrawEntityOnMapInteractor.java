package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import io.reactivex.Observable;
import java.util.Collections;

@AutoFactory
public class DrawEntityOnMapInteractor extends BaseDataInteractor {

  private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;
  private final GeoEntity mGeoEntity;
  private GeoEntitiesRepository mGeoEntitiesRepository;

  DrawEntityOnMapInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided DisplayedEntitiesRepository displayedEntitiesRepository,
      @Provided GeoEntitiesRepository geoEntitiesRepository,
      GeoEntity geoEntity) {
    super(threadExecutor);
    mDisplayedEntitiesRepository = displayedEntitiesRepository;
    mGeoEntitiesRepository = geoEntitiesRepository;
    mGeoEntity = geoEntity;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest subscriptionRequest = factory.create(Observable.just(mGeoEntity),
        geoEntityObservable -> geoEntityObservable.doOnNext(this::draw));
    return Collections.singletonList(subscriptionRequest);
  }

  private void draw(GeoEntity geoEntity) {
    mGeoEntitiesRepository.add(geoEntity);
    mDisplayedEntitiesRepository.show(geoEntity);
  }
}
