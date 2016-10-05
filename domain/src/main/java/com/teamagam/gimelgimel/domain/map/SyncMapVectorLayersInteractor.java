package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import rx.Observable;
import rx.Subscriber;

/**
 * {@link SyncInteractor} with generic type {@link GeoEntityNotification} to sync the displayed map with it's
 * repository. all of the entities that appear on the map should be in the
 * {@link DisplayedEntitiesRepository}.
 */
@AutoFactory
public class SyncMapVectorLayersInteractor extends SyncInteractor<GeoEntityNotification> {

    private final DisplayedEntitiesRepository mDisplayedRepo;

    SyncMapVectorLayersInteractor(@Provided ThreadExecutor threadExecutor,
                                  @Provided PostExecutionThread postExecutionThread,
                                  @Provided DisplayedEntitiesRepository mapRepo,
                                  Subscriber<GeoEntityNotification> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mDisplayedRepo = mapRepo;
    }

    @Override
    protected Observable<GeoEntityNotification> buildUseCaseObservable() {
        return mDisplayedRepo.getSyncEntitiesObservable();
    }
}
