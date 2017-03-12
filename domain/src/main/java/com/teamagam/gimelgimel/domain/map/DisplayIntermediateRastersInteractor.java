package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.IntermediateRaster;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;

@AutoFactory
public class DisplayIntermediateRastersInteractor extends BaseSingleDisplayInteractor {

    private final SingleDisplayedItemRepository<IntermediateRaster>
            mCurrentIntermediateRasterRepository;
    private final Renderer mRenderer;

    public DisplayIntermediateRastersInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided SingleDisplayedItemRepository<IntermediateRaster>
                    currentIntermediateRasterRepository,
            Renderer renderer) {
        super(threadExecutor, postExecutionThread);
        mCurrentIntermediateRasterRepository = currentIntermediateRasterRepository;
        mRenderer = renderer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.create(mCurrentIntermediateRasterRepository
                        .getDisplayEventsObservable(),
                this::updateRenderer);
    }

    private void updateRenderer(SingleDisplayedItemRepository.DisplayEvent displayEvent) {
        if (displayEvent == SingleDisplayedItemRepository.DisplayEvent.DISPLAY) {
            mRenderer.setCurrent(mCurrentIntermediateRasterRepository.getCurrentDisplayedItem());
        } else {
            mRenderer.removeCurrent();
        }
    }

    public interface Renderer {
        void setCurrent(IntermediateRaster intermediateRaster);

        void removeCurrent();
    }
}
