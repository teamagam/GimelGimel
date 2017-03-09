package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.IntermediateRaster;
import com.teamagam.gimelgimel.domain.map.repository.CurrentIntermediateRasterRepository;

@AutoFactory
public class DisplayRastersInteractor extends BaseSingleDisplayInteractor {

    private final CurrentIntermediateRasterRepository mCurrentIntermediateRasterRepository;
    private final Renderer mRenderer;

    public DisplayRastersInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided CurrentIntermediateRasterRepository currentIntermediateRasterRepository,
            Renderer renderer) {
        super(threadExecutor, postExecutionThread);
        mCurrentIntermediateRasterRepository = currentIntermediateRasterRepository;
        mRenderer = renderer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.create(mCurrentIntermediateRasterRepository
                        .getIntermediateRasterEventsObservable(),
                this::updateRenderer);
    }

    private void updateRenderer(CurrentIntermediateRasterRepository.DisplayEvent displayEvent) {
        if (displayEvent == CurrentIntermediateRasterRepository.DisplayEvent.DISPLAY) {
            mRenderer.setCurrent(mCurrentIntermediateRasterRepository.getCurrentIntermediateRaster());
        } else {
            mRenderer.removeCurrent();
        }
    }

    public interface Renderer {
        void setCurrent(IntermediateRaster intermediateRaster);

        void removeCurrent();
    }
}
