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
                buildObservable());
        return Collections.singletonList(dataSubscriptionRequest);
    }

    private Observable<URI> buildObservable() {
        return Observable.just(mVectorLayer)
                .flatMap(this::processIfNeeded);
    }

    private Observable<URI> processIfNeeded(VectorLayer vl) {
        if (isOutdatedVectorLayer(vl)) {
            sLogger.d("Not processing following vector layer because it's outdated: " + vl);
            return Observable.empty();
        }
        sLogger.d("Processing vector layer " + vl);
        return buildProcessObservable(vl);
    }

    private boolean isOutdatedVectorLayer(VectorLayer vl) {
        return mVectorLayerRepository.contains(vl.getId()) &&
                mVectorLayerRepository.get(vl.getId()).getVersion() > vl.getVersion();
    }

    private Observable<URI> buildProcessObservable(VectorLayer vectorLayer) {
        return Observable.just(vectorLayer)
                .flatMap(this::cacheLayer)
                .doOnNext(uri -> sLogger.d("Vector layer " + vectorLayer + " is cached at " + uri))
                .doOnNext(uri -> addToRepository())
                .doOnNext(uri -> setVisible())
                .retryWhen(new RetryWithDelay(Constants.LAYER_CACHING_RETRIES,
                        Constants.LAYER_CACHING_RETRIES_DELAY_MS))
                .doOnError(this::logFailure)
                .onErrorResumeNext(Observable.empty());
    }

    private Observable<URI> cacheLayer(VectorLayer vectorLayer) {
        if (mLayersLocalCache.isCached(vectorLayer)) {
            return Observable.just(mLayersLocalCache.getCachedURI(vectorLayer));
        } else if (mUrl != null) {
            return mLayersLocalCache.cache(vectorLayer, mUrl);
        }
        throw new RuntimeException(String.format(
                "VectorLayer '%s' is not cached but URL was not supplied.",
                mVectorLayer.getName()));
    }

    private void addToRepository() {
        mVectorLayerRepository.put(mVectorLayer);
    }

    private void setVisible() {
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(
                new VectorLayerVisibilityChange(mVectorLayer.getId(), true));
    }

    private void logFailure(Throwable throwable) {
        sLogger.w("Couldn't cache layer " + mVectorLayer, throwable);
    }
}
