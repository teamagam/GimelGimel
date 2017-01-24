package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

@AutoFactory
public class DisplayVectorLayersInteractor extends BaseSingleDisplayInteractor {

    private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
    private final Displayer mDisplayer;

    public DisplayVectorLayersInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
        mDisplayer = displayer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.create(
                mVectorLayersVisibilityRepository.getVisibilityChangesLogObservable(),
                visibilityChange -> mDisplayer.setVisibility(visibilityChange.getVectorLayer(),
                        visibilityChange.getVisibility()));
    }

    public interface Displayer {
        void setVisibility(VectorLayer vectorLayer, boolean visibility);
    }
}
