package com.teamagam.gimelgimel.domain.layers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.net.URI;
import java.util.Collections;

import rx.Observable;

@AutoFactory
public class ProcessIncomingVectorLayerInteractor extends BaseDataInteractor {

    private static final Logger sLogger = LoggerFactory.create(
            ProcessIncomingVectorLayerInteractor.class.getSimpleName());

    private final LayersLocalCache mLayersLocalCache;
    private final VectorLayer mVectorLayer;

    public ProcessIncomingVectorLayerInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided LayersLocalCache layersLocalCache,
            VectorLayer vectorLayer) {
        super(threadExecutor);
        mLayersLocalCache = layersLocalCache;
        mVectorLayer = vectorLayer;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest dataSubscriptionRequest = factory.create(
                buildProcessingObservable());
        return Collections.singletonList(dataSubscriptionRequest);
    }

    private Observable<URI> buildProcessingObservable() {
        return Observable.just(mVectorLayer)
                .flatMap(this::fetchCachedURI)
                .doOnNext(uri -> sLogger.i("VectorLayer cached uri:" + uri.toString()))
                .retryWhen(new RetryWithDelay(Constants.LAYER_CACHING_RETRIES,
                        Constants.LAYER_CACHING_RETRIES_DELAY_MS))
                .doOnError(throwable -> sLogger.w("Couldn't cache layer " + mVectorLayer))
                .onErrorResumeNext(Observable.empty());
    }

    private Observable<URI> fetchCachedURI(VectorLayer vl) {
        if (mLayersLocalCache.isCached(vl)) {
            return Observable.just(mLayersLocalCache.getCachedURI(vl));
        } else {
            return mLayersLocalCache.cache(vl);
        }
    }
}
