package com.teamagam.gimelgimel.domain.base.interactors;


import rx.Subscriber;

/**
 * This is the main interface of an interactor. Each interactor serves a specific use case.
 */
public interface Interactor<T>{

    /**
     * This is the main method that starts an interactor. It will make sure that the interactor operation is done on a
     * background thread.
     */
    void execute(Subscriber<T> useCaseSubscriber);
}
