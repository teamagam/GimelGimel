package com.teamagam.gimelgimel.domain.location;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;

import javax.inject.Inject;

import rx.Observable;

public class StartLocationUpdatesInteractor extends DoInteractor<LocationEventFetcher> {

    private LocationEventFetcher mLocationEventFetcher;

    @Inject
    public StartLocationUpdatesInteractor(
            LocationEventFetcher locationEventFetcher,
            ThreadExecutor threadExecutor) {
        super(threadExecutor);

        mLocationEventFetcher = locationEventFetcher;
    }

    @Override
    protected Observable<LocationEventFetcher> buildUseCaseObservable() {
        return Observable.just(mLocationEventFetcher)
                .doOnNext(LocationEventFetcher::startFetching);
    }
}
