package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;

import rx.Observable;
import rx.Subscriber;

/**
 * Created on 11/2/2016.
 * TODO: complete text
 */
@AutoFactory
public class GetMapEntityInteractor extends SyncInteractor<GeoEntity>{

    private GeoEntitiesRepository mGeoEntitiesRepository;
    private String mGeoEntityId;

    protected GetMapEntityInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided GeoEntitiesRepository geoEntitiesRepository,
            Subscriber<GeoEntity> useCaseSubscriber,
            String geoEntityId) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mGeoEntitiesRepository = geoEntitiesRepository;
        mGeoEntityId = geoEntityId;
    }

    @Override
    protected Observable<GeoEntity> buildUseCaseObservable() {
        return Observable.just(mGeoEntityId)
                .map(mGeoEntitiesRepository::get);
    }
}
