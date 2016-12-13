package com.teamagam.gimelgimel.domain.notifications;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

import java.util.Collections;

class DisplayConnectivityStatusInteractor extends BaseDisplayInteractor {

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
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return Collections.singletonList(factory.create(mConnectivityRepository.getObservable(),
                connectivityStatus -> {
                    if (connectivityStatus.isConnected()) {
                        mDisplayer.connectivityOn();
                    } else {
                        mDisplayer.connectivityOff();
                    }
                }));
    }
}
