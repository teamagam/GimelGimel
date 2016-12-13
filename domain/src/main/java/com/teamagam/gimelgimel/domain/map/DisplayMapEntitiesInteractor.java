package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.util.Collections;

@AutoFactory
public class DisplayMapEntitiesInteractor extends BaseDisplayInteractor {

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
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return Collections.singletonList(
                factory.create(
                        mDisplayedRepo.getDisplayedGeoEntitiesObservable()
                                .flatMapIterable(entities -> entities)
                                .map(GeoEntityNotification::createAdd)
                                .concatWith(mDisplayedRepo.getSyncEntitiesObservable()),
                        mDisplayer::displayEntityNotification));
    }

    public interface Displayer {
        void displayEntityNotification(GeoEntityNotification geoEntity);
    }
}
