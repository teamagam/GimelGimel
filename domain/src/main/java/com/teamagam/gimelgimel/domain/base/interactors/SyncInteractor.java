package com.teamagam.gimelgimel.domain.base.interactors;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents an execution unit for different data synchronization use cases
 * <p>
 * By convention each synchronization UseCase implementation will return the result using
 * a {@link rx.Subscriber} that will execute its job in a background thread and will post the
 * result in the UI thread.
 */
public abstract class SyncInteractor<T> extends AbsInteractor<T> {

    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private Subscriber<T> mSubscriber;

    protected SyncInteractor(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             Subscriber<T> useCaseSubscriber) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        mSubscriber = useCaseSubscriber;
    }

    /**
     * Builds an {@link rx.Observable} which will be used when executing the current {@link SyncInteractor}.
     */
    protected Observable<T> buildObservable() {
        return buildUseCaseObservable()
                .subscribeOn(threadExecutor.getScheduler())
                .observeOn(postExecutionThread.getScheduler());
    }

    protected abstract Observable<T> buildUseCaseObservable();

    @Override
    protected Subscriber<T> getSubscriber() {
        return mSubscriber;
    }
}
