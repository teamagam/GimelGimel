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

import rx.functions.Action1;

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
                mVectorLayersVisibilityRepository.getVisibilityChangesLogObservable(),
                getVectorLayerDisplayAction());
    }

    private Action1<VectorLayerVisibilityChange> getVectorLayerDisplayAction() {
        return change -> display(
                mVectorLayersRepository.get(change.getVectorLayerId()), change.getVisibility());
    }

    private void display(VectorLayer vectorLayer, boolean isVisible) {
        URI localUri = mLayersLocalCache.getCachedURI(vectorLayer);
        if (isVisible) {
            mDisplayer.displayShown(VectorLayerPresentation.create(vectorLayer, localUri));
        } else {
            mDisplayer.displayHidden(VectorLayerPresentation.create(vectorLayer, localUri));
        }
    }

    public interface Displayer {
        void displayShown(VectorLayerPresentation vectorLayer);

        void displayHidden(VectorLayerPresentation vectorLayer);
    }
}
