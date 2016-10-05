package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.util.Collection;

import rx.Observable;
import rx.Subscriber;

/**
 * {@link SyncInteractor} with generic type {@link GeoEntityNotification} to sync the displayed map with it's
 * repository. all of the entities that appear on the map should be in the
 * {@link DisplayedEntitiesRepository}.
 */
@AutoFactory
public class GetMapVectorLayersInteractor extends SyncInteractor<Collection<GeoEntity>> {

    private final DisplayedEntitiesRepository mDisplayedRepo;

    GetMapVectorLayersInteractor(@Provided ThreadExecutor threadExecutor,
                                 @Provided PostExecutionThread postExecutionThread,
                                 @Provided DisplayedEntitiesRepository mapRepo,
                                 Subscriber<Collection<GeoEntity>> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mDisplayedRepo = mapRepo;
    }

    @Override
    protected Observable<Collection<GeoEntity>> buildUseCaseObservable() {
        return mDisplayedRepo.getDisplayedGeoEntitiesObservable();
    }
}
