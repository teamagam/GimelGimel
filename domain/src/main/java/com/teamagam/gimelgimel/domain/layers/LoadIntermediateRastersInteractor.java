package com.teamagam.gimelgimel.domain.layers;


import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.IntermediateRastersRepository;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observable;

public class LoadIntermediateRastersInteractor extends BaseDataInteractor {

    private final IntermediateRasterLocalStorage mRastersLocalStorage;
    private final IntermediateRastersRepository mRastersRepository;

    @Inject
    public LoadIntermediateRastersInteractor(ThreadExecutor threadExecutor,
                                             IntermediateRasterLocalStorage rasterLocalStorage,
                                             IntermediateRastersRepository rastersRepository) {
        super(threadExecutor);
        mRastersLocalStorage = rasterLocalStorage;
        mRastersRepository = rastersRepository;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest subscriptionRequest = factory.create(Observable.just(null)
                .flatMapIterable(x -> mRastersLocalStorage.getExistingRasters())
                .doOnNext(mRastersRepository::add));

        return Collections.singleton(subscriptionRequest);
    }
}
