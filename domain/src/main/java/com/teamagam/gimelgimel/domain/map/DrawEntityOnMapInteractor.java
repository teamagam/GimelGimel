package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class DrawEntityOnMapInteractor extends BaseDataInteractor {

    private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private final GeoEntity mGeoEntity;

    DrawEntityOnMapInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided DisplayedEntitiesRepository displayedEntitiesRepository,
            GeoEntity geoEntity) {
        super(threadExecutor);
        mDisplayedEntitiesRepository = displayedEntitiesRepository;
        mGeoEntity = geoEntity;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest subscriptionRequest = factory.create(
                Observable.just(mGeoEntity)
                        .doOnNext(mDisplayedEntitiesRepository::show)
        );
        return Collections.singletonList(subscriptionRequest);
    }
}
