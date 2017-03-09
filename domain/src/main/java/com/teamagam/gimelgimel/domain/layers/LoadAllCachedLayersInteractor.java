package com.teamagam.gimelgimel.domain.layers;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observable;

public class LoadAllCachedLayersInteractor extends BaseDataInteractor {

    private final LayersLocalCache mLayersLocalCache;
    private final ProcessNewVectorLayerInteractorFactory mProcessNewVectorLayerInteractorFactory;

    @Inject
    public LoadAllCachedLayersInteractor(ThreadExecutor threadExecutor,
                                         LayersLocalCache layersLocalCache,
                                         ProcessNewVectorLayerInteractorFactory
                                                 processNewVectorLayerInteractorFactory) {
        super(threadExecutor);
        mLayersLocalCache = layersLocalCache;
        mProcessNewVectorLayerInteractorFactory = processNewVectorLayerInteractorFactory;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest request = factory.create(createObservable());
        return Collections.singletonList(request);
    }

    private Observable<VectorLayer> createObservable() {
        return Observable.just(null)
                .flatMapIterable(x -> mLayersLocalCache.getAllCachedLayers())
                .map(this::recreateAsUnimportant)
                .doOnNext(vl -> mProcessNewVectorLayerInteractorFactory.create(vl, null).execute());
    }

    private VectorLayer recreateAsUnimportant(VectorLayer vectorLayer) {
        return VectorLayer.copyWithDifferentSeverity(vectorLayer, VectorLayer.Severity.REGULAR);
    }
}
