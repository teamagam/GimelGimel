package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.IntermediateRaster;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class SetIntermediateRasterInteractor extends BaseDataInteractor {

    private final IntermediateRaster mIntermediateRaster;
    private final SingleDisplayedItemRepository<IntermediateRaster> mSingleDisplayedItemRepository;

    public SetIntermediateRasterInteractor(@Provided ThreadExecutor threadExecutor,
                                           @Provided SingleDisplayedItemRepository<IntermediateRaster>
                                                   singleDisplayedItemRepository,
                                           IntermediateRaster intermediateRaster) {
        super(threadExecutor);
        mSingleDisplayedItemRepository = singleDisplayedItemRepository;
        mIntermediateRaster = intermediateRaster;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        SubscriptionRequest setRasterRequest = factory.create(
                Observable.just(mIntermediateRaster)
                        .doOnNext(mSingleDisplayedItemRepository::setCurrentDisplayedItem));
        return Collections.singletonList(setRasterRequest);
    }
}
