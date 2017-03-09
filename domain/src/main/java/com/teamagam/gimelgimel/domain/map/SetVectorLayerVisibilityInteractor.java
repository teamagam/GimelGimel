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

    private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
    private final VectorLayerVisibilityChange mChange;

    public SetVectorLayerVisibilityInteractor(@Provided ThreadExecutor threadExecutor,
                                              @Provided VectorLayersVisibilityRepository
                                                      vectorLayersVisibilityRepository,
                                              VectorLayerVisibilityChange change
    ) {
        super(threadExecutor);
        mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
        mChange = change;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        SubscriptionRequest setVisibilityRequest = factory.create(
                Observable.just(mChange),
                vectorLayerVisibilityChangeObservable ->
                        vectorLayerVisibilityChangeObservable
                                .doOnNext(
                                        mVectorLayersVisibilityRepository::changeVectorLayerVisibility));
        return Collections.singletonList(setVisibilityRequest);
    }
}
