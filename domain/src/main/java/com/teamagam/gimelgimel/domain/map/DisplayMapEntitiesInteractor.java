package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import rx.Observable;

/**
 * Created on 11/13/2016.
 * TODO: complete text
 */
@AutoFactory
public class DisplayMapEntitiesInteractor extends SyncInteractor<GeoEntityNotification> {

    private final DisplayedEntitiesRepository mDisplayedRepo;

    protected DisplayMapEntitiesInteractor(@Provided ThreadExecutor threadExecutor,
                                           @Provided PostExecutionThread postExecutionThread,
                                           @Provided DisplayedEntitiesRepository mapRepo,
                                           Displayer displayer) {
        super(threadExecutor, postExecutionThread, new ShowDisplayedEntitySubscriber(displayer));
        mDisplayedRepo = mapRepo;
    }

    @Override
    protected Observable<GeoEntityNotification> buildUseCaseObservable() {
        return mDisplayedRepo.getDisplayedGeoEntitiesObservable()
                .flatMapIterable(entities -> entities)
                .map(GeoEntityNotification::createAdd)
                .concatWith(mDisplayedRepo.getSyncEntitiesObservable());

    }

    private static class ShowDisplayedEntitySubscriber extends SimpleSubscriber<GeoEntityNotification> {

        private Displayer mDisplayer;

        private ShowDisplayedEntitySubscriber(Displayer displayer) {
            mDisplayer = displayer;
        }

        @Override
        public void onNext(GeoEntityNotification geoEntityNotification) {
            mDisplayer.displayEntityNotification(geoEntityNotification);
        }

        @Override
        public void onError(Throwable e) {
//            super.onError(e);
//            sLogger.e("point next error: ", e);
        }

    }

    public interface Displayer {
        void displayEntityNotification(GeoEntityNotification geoEntity);
    }

}
