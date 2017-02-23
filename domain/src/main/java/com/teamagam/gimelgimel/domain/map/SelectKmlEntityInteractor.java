package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.CurrentlyPresentedKmlEntityInfoRepository;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class SelectKmlEntityInteractor extends BaseDataInteractor {

    private final CurrentlyPresentedKmlEntityInfoRepository mCurrentlyPresentedKmlEntityInfoRepository;
    private final KmlEntityInfo mKmlEntityInfo;

    public SelectKmlEntityInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided CurrentlyPresentedKmlEntityInfoRepository
                    currentlyPresentedKmlEntityInfoRepository,
            KmlEntityInfo kmlEntityInfo) {
        super(threadExecutor);
        mCurrentlyPresentedKmlEntityInfoRepository = currentlyPresentedKmlEntityInfoRepository;
        mKmlEntityInfo = kmlEntityInfo;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest dataSubscriptionRequest = factory.create(getObservable());
        return Collections.singletonList(dataSubscriptionRequest);
    }

    private Observable<KmlEntityInfo> getObservable() {
        return Observable.just(mKmlEntityInfo)
                .doOnNext(mCurrentlyPresentedKmlEntityInfoRepository::
                        setCurrentlyPresentedKmlEntityInfo);
    }
}
