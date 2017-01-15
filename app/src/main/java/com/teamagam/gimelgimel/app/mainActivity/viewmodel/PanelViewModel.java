package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractor;
import com.teamagam.gimelgimel.domain.messages.UpdateMessagesReadInteractorFactory;

import javax.inject.Inject;

public class PanelViewModel {

    protected AppLogger sLogger = AppLoggerFactory.create();
    @Inject
    UpdateMessagesReadInteractorFactory mInteractorFactory;

    @Inject
    PanelViewModel() {
    }

    public void changePanelState(SlidingUpPanelLayout.PanelState newState) {
        if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED
                || newState == SlidingUpPanelLayout.PanelState.HIDDEN) {
            executeUpdateMessagesReadInteractor();
        }
    }

    private void executeUpdateMessagesReadInteractor() {
        UpdateMessagesReadInteractor interactor = mInteractorFactory.create();
        interactor.execute();
    }
}
