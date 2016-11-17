package com.teamagam.gimelgimel.domain.location;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

import rx.Observable;
import rx.Subscriber;

@AutoFactory
public class GetLocationStreamInteractor extends SyncInteractor<LocationSample> {

    private LocationRepository mLocationRepository;

    protected GetLocationStreamInteractor(@Provided ThreadExecutor threadExecutor,
                                          @Provided PostExecutionThread postExecutionThread,
                                          @Provided LocationRepository locationRepository,
                                          Subscriber<LocationSample> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);

        mLocationRepository = locationRepository;
    }

    @Override
    protected Observable<LocationSample> buildUseCaseObservable() {
        return mLocationRepository.getLocationObservable();
    }
}
