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
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
import com.teamagam.gimelgimel.domain.notifications.entity.VectorLayerVisibilityChange;

import java.net.URI;
import java.net.URL;
import java.util.Collections;

import rx.Observable;

@AutoFactory
public class ProcessNewVectorLayerInteractor extends BaseDataInteractor {

    private static final Logger sLogger = LoggerFactory.create(
            ProcessNewVectorLayerInteractor.class.getSimpleName());

    private final LayersLocalCache mLayersLocalCache;
    private final VectorLayer mVectorLayer;
    private final VectorLayersRepository mVectorLayerRepository;
    private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
    private URL mUrl;

    ProcessNewVectorLayerInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided LayersLocalCache layersLocalCache,
            @Provided VectorLayersRepository vectorLayerRepository,
            @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
            VectorLayer vectorLayer) {
        this(threadExecutor,
                layersLocalCache, vectorLayerRepository,
                vectorLayersVisibilityRepository, vectorLayer, null);
    }

    ProcessNewVectorLayerInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided LayersLocalCache layersLocalCache,
            @Provided VectorLayersRepository vectorLayerRepository,
            @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
            VectorLayer vectorLayer,
            URL url) {
        super(threadExecutor);
        mLayersLocalCache = layersLocalCache;
        mVectorLayerRepository = vectorLayerRepository;
        mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
        mVectorLayer = vectorLayer;
        mUrl = url;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest dataSubscriptionRequest = factory.create(
                buildProcessingObservable());
        return Collections.singletonList(dataSubscriptionRequest);
    }

    private Observable<URI> buildProcessingObservable() {
        return Observable.just(null)
                .flatMap(x -> fetchCachedURI())
                .doOnNext(uri -> sLogger.i("VectorLayer cached uri:" + uri.toString()))
                .doOnNext(uri -> addToRepository())
                .doOnNext(uri -> setVisible())
                .retryWhen(new RetryWithDelay(Constants.LAYER_CACHING_RETRIES,
                        Constants.LAYER_CACHING_RETRIES_DELAY_MS))
                .doOnError(throwable -> sLogger.w("Couldn't cache layer " + mVectorLayer.getName()))
                .onErrorResumeNext(Observable.empty());
    }

    private Observable<URI> fetchCachedURI() {
        if (mLayersLocalCache.isCached(mVectorLayer)) {
            return Observable.just(mLayersLocalCache.getCachedURI(mVectorLayer));
        } else if (mUrl != null){
            return mLayersLocalCache.cache(mVectorLayer, mUrl);
        }
        throw new RuntimeException(String.format(
                "VectorLayer '%s' is not cached but URL was not supplied.",
                mVectorLayer.getName()));
    }

    private void addToRepository() {
        mVectorLayerRepository.add(mVectorLayer);
    }

    private void setVisible() {
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(
                new VectorLayerVisibilityChange(mVectorLayer.getId(), true));
    }
}
