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
        DataSubscriptionRequest<?> request = factory.create(
                Observable.just(mLayersLocalCache),
                this::loadCachedLayers);
        return Collections.singletonList(request);
    }

    private Observable<VectorLayer> loadCachedLayers(Observable<LayersLocalCache> observable) {
        return observable
                .flatMapIterable(LayersLocalCache::getAllCachedLayers)
                .doOnNext(vl -> mProcessNewVectorLayerInteractorFactory.create(vl, null).execute());
    }
}
