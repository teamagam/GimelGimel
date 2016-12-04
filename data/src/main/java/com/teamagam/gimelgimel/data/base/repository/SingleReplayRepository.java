package com.teamagam.gimelgimel.data.base.repository;

import rx.Observable;
import rx.subjects.PublishSubject;

public class SingleReplayRepository<T> {

    private final PublishSubject<T> mSubject;
    private final Observable<T> mObservable;

    public SingleReplayRepository() {
        mSubject = PublishSubject.create();
        mObservable = mSubject.replay(1).autoConnect();
        mObservable.subscribe();
    }

    public void setValue(T newValue) {
        mSubject.onNext(newValue);
    }

    public Observable<T> getObservable() {
        return mObservable;
    }
}
