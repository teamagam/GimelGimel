package com.teamagam.gimelgimel.domain.notifications;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

import javax.inject.Named;

@AutoFactory
public class DisplayDataConnectivityStatusInteractor extends DisplayConnectivityStatusInteractor {

    protected DisplayDataConnectivityStatusInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided @Named("data") ConnectivityStatusRepository connectivityRepository,
            ConnectivityDisplayer displayer) {
        super(threadExecutor, postExecutionThread, connectivityRepository, displayer);
    }
}
