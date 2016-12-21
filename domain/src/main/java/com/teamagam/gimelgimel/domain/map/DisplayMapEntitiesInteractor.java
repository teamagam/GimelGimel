package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

@AutoFactory
public class DisplayMapEntitiesInteractor extends BaseSingleDisplayInteractor {

    private final DisplayedEntitiesRepository mDisplayedRepo;
    private final Displayer mDisplayer;

    protected DisplayMapEntitiesInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided DisplayedEntitiesRepository mapRepo,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mDisplayedRepo = mapRepo;
        mDisplayer = displayer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.create(
                mDisplayedRepo.getDisplayedGeoEntitiesObservable()
                        .flatMapIterable(entities -> entities)
                        .map(GeoEntityNotification::createAdd)
                        .concatWith(mDisplayedRepo.getSyncEntitiesObservable()),
                mDisplayer::displayEntityNotification);
    }

    public interface Displayer {
        void displayEntityNotification(GeoEntityNotification geoEntity);
    }
}
