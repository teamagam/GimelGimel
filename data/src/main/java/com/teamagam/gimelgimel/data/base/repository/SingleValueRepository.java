package com.teamagam.gimelgimel.data.base.repository;

import rx.Observable;
import rx.subjects.PublishSubject;

public class SingleValueRepository<T> {

    private PublishSubject<T> mSubject;

    public SingleValueRepository() {
        mSubject = PublishSubject.create();
    }

    public void setValue(T newValue) {
        mSubject.onNext(newValue);
    }

    public Observable<T> getObservable() {
        return mSubject.replay(1);
    }
}
