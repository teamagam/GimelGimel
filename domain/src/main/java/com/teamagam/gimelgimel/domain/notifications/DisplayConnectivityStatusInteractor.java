package com.teamagam.gimelgimel.domain.notifications;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

class DisplayConnectivityStatusInteractor extends BaseSingleDisplayInteractor {

    private final ConnectivityStatusRepository mConnectivityRepository;
    private final ConnectivityDisplayer mDisplayer;

    DisplayConnectivityStatusInteractor(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ConnectivityStatusRepository connectivityRepository,
            ConnectivityDisplayer displayer) {
        super(threadExecutor, postExecutionThread);
        mConnectivityRepository = connectivityRepository;
        mDisplayer = displayer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.createSimple(
                mConnectivityRepository.getObservable(),
                connectivityStatus -> {
                    if (connectivityStatus.isConnected()) {
                        mDisplayer.connectivityOn();
                    } else {
                        mDisplayer.connectivityOff();
                    }
                }
        );
    }
}
