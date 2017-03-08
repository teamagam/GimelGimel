package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.VectorLayerVisibilityChange;

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
                Observable.just(null),
                this::changeVisibility);
        return Collections.singletonList(setVisibilityRequest);
    }

    private Observable<VectorLayerVisibilityChange> changeVisibility(
            Observable<Object> initiatingObservable) {
        return initiatingObservable
                .map(x -> new VectorLayerVisibilityChange(mVectorLayerId, mIsVisible))
                .doOnNext(
                        mVectorLayersVisibilityRepository::changeVectorLayerVisibility);
    }
}
