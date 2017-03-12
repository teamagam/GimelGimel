package com.teamagam.gimelgimel.domain.layers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class SetVectorLayerVisibilityInteractor extends BaseDataInteractor {

    private final String mVectorLayerId;
    private final boolean mIsVisible;
    private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;

    public SetVectorLayerVisibilityInteractor(@Provided ThreadExecutor threadExecutor,
                                              @Provided VectorLayersVisibilityRepository
                                                      vectorLayersVisibilityRepository,
                                              String vectorLayerId,
                                              boolean isVisible) {
        super(threadExecutor);
        mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
        mVectorLayerId = vectorLayerId;
        mIsVisible = isVisible;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        SubscriptionRequest setVisibilityRequest = factory.create(
                Observable.just(new VectorLayerVisibilityChange(mVectorLayerId, mIsVisible))
                        .doOnNext(mVectorLayersVisibilityRepository::addChange));
        return Collections.singletonList(setVisibilityRequest);
    }
}
