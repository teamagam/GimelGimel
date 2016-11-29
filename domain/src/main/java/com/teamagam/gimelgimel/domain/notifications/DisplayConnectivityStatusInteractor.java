package com.teamagam.gimelgimel.domain.notifications;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.Interactor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;

import rx.Subscription;

class DisplayConnectivityStatusInteractor implements Interactor {

    private final PostExecutionThread mPostExecutionThread;
    private final ThreadExecutor mThreadExecutor;
    private final ConnectivityStatusRepository mConnectivityRepository;
    private final ConnectivityDisplayer mDisplayer;

    private Subscription mSubscription;

    DisplayConnectivityStatusInteractor(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ConnectivityStatusRepository connectivityRepository,
            ConnectivityDisplayer displayer) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mConnectivityRepository = connectivityRepository;
        mDisplayer = displayer;
    }

    @Override
    public void execute() {
        mSubscription = mConnectivityRepository.getObservable()
                .subscribeOn(mThreadExecutor.getScheduler())
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(new SimpleSubscriber<ConnectivityStatus>() {
                    @Override
                    public void onNext(ConnectivityStatus connectivityStatus) {
                        if (connectivityStatus.isConnected()) {
                            mDisplayer.connectivityOn();
                        } else {
                            mDisplayer.connectivityOff();
                        }
                    }
                });
    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
