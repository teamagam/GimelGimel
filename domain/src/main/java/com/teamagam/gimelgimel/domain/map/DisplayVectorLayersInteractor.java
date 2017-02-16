package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
import com.teamagam.gimelgimel.domain.notifications.entity.VectorLayerVisibilityChange;

import java.net.URI;

@AutoFactory
public class DisplayVectorLayersInteractor extends BaseSingleDisplayInteractor {

    private final VectorLayersRepository mVectorLayersRepository;
    private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
    private final LayersLocalCache mLayersLocalCache;
    private final Displayer mDisplayer;

    public DisplayVectorLayersInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided VectorLayersRepository vectorLayersRepository,
            @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
            @Provided LayersLocalCache layersLocalCache,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mVectorLayersRepository = vectorLayersRepository;
        mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
        mLayersLocalCache = layersLocalCache;
        mDisplayer = displayer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.create(
                mVectorLayersVisibilityRepository.getVisibilityChangesLogObservable()
                        .map(this::createVectorLayerPresentation),
                mDisplayer::display);
    }

    private VectorLayerPresentation createVectorLayerPresentation(
            VectorLayerVisibilityChange visibilityChange) {
        VectorLayer vl = mVectorLayersRepository.get(visibilityChange.getVectorLayerId());
        URI cachedURI = mLayersLocalCache.getCachedURI(vl);
        if (visibilityChange.getVisibility()) {
            return VectorLayerPresentation.createShown(vl, cachedURI);
        }
        return VectorLayerPresentation.createHidden(vl, cachedURI);
    }

    public interface Displayer {
        void display(VectorLayerPresentation vectorLayerPresentation);
    }
}
