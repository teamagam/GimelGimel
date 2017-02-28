package com.teamagam.gimelgimel.domain.notifications;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

import javax.inject.Named;

@AutoFactory
public class Display3GConnectivityStatusInteractor extends DisplayConnectivityStatusInteractor {
    Display3GConnectivityStatusInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided @Named("3g") ConnectivityStatusRepository connectivityRepository,
            ConnectivityDisplayer displayer) {
        super(threadExecutor, postExecutionThread, connectivityRepository, displayer);
    }
}