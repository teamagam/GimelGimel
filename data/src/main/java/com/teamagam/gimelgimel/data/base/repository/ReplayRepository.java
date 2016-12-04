package com.teamagam.gimelgimel.data.base.repository;

import rx.Observable;
import rx.subjects.PublishSubject;

public class ReplayRepository<T> {

    private PublishSubject<T> mPublishSubject;
    private Observable<T> mObservable;

    public ReplayRepository() {
        mPublishSubject = PublishSubject.create();
        mObservable = mPublishSubject.replay().autoConnect();
        mObservable.subscribe();
    }

    public void add(T value) {
        mPublishSubject.onNext(value);
    }

    public Observable<T> getObservable() {
        return mObservable;
    }
}
