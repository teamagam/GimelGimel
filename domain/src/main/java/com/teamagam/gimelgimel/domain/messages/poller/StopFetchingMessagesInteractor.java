package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.poller.strategy.RepeatedBackoffTaskRunner;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;

/**
 * Start fetching interactor logic. using {@link RepeatedBackoffTaskRunner}.
 */
public class StopFetchingMessagesInteractor extends DoInteractor{

    private final RepeatedBackoffTaskRunner mMessagesTaskRunner;

    @Inject
    public StopFetchingMessagesInteractor(
            ThreadExecutor threadExecutor,
            @Named("message poller") RepeatedBackoffTaskRunner taskRunner) {
        super(threadExecutor);
        mMessagesTaskRunner = taskRunner;
    }


    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.just(mMessagesTaskRunner)
                .doOnNext(RepeatedBackoffTaskRunner::stopNextExecutions);
    }
}
