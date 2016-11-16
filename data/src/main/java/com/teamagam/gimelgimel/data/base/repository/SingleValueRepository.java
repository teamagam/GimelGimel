package com.teamagam.gimelgimel.data.base.repository;

import rx.Observable;
import rx.subjects.PublishSubject;

public class SingleValueRepository<T> {

    private PublishSubject<T> mSubject;
    private Observable<T> mObservable;

    public SingleValueRepository() {
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
