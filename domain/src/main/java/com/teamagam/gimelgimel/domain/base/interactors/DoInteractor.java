package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;

/**
 * Base interactor for acting on model to change data
 */
public abstract class DoInteractor<T> implements Interactor {

    private final ThreadExecutor threadExecutor;

    protected DoInteractor(ThreadExecutor threadExecutor) {
        this.threadExecutor = threadExecutor;
    }

    /**
     * Executes the current use case.
     */
    @Override
    public void execute() {
        buildUseCaseObservable()
                .subscribeOn(threadExecutor.getScheduler())
                .subscribe(createSubscriber());
    }

    /**
     * Builds an {@link rx.Observable} which will be used when executing the current {@link DoInteractor}.
     */
    protected abstract Observable<T> buildUseCaseObservable();

    /**
     * Creates subscriber that handles interactor's observable observed items.
     * Override to change behaviour.
     */
    protected  Subscriber<T> createSubscriber(){
        return new DoNothingSubscriber<>();
    }

}
