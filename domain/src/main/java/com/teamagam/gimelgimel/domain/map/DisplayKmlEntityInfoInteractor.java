package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.CurrentKmlEntityInfoRepository;

@AutoFactory
public class DisplayKmlEntityInfoInteractor extends BaseSingleDisplayInteractor {

    private final CurrentKmlEntityInfoRepository mCurrentKmlEntityInfoRepository;
    private final Displayer mDisplayer;

    public DisplayKmlEntityInfoInteractor(@Provided ThreadExecutor threadExecutor,
                                          @Provided PostExecutionThread postExecutionThread,
                                          @Provided CurrentKmlEntityInfoRepository
                                                  currentKmlEntityInfoRepository,
                                          Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mCurrentKmlEntityInfoRepository = currentKmlEntityInfoRepository;
        mDisplayer = displayer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.createSimple(mCurrentKmlEntityInfoRepository
                .getKmlEntityInfoEventsObservable(), this::updateDisplayer);
    }

    public void updateDisplayer(CurrentKmlEntityInfoRepository.KmlEntityInfoEvent event) {
        if (event == CurrentKmlEntityInfoRepository.KmlEntityInfoEvent.DISPLAY) {
            mDisplayer.display(mCurrentKmlEntityInfoRepository.getCurrentKmlEntityInfo());
        } else {
            mDisplayer.hide();
        }
    }

    public interface Displayer {
        void display(KmlEntityInfo kmlEntityInfo);

        void hide();
    }
}
