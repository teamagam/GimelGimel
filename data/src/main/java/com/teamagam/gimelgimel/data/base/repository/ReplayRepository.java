package com.teamagam.gimelgimel.data.base.repository;

import rx.Observable;
import rx.subjects.PublishSubject;

public class ReplayRepository<T> {

    private final PublishSubject<T> mPublishSubject;
    private final Observable<T> mObservable;

    public static <T> ReplayRepository<T> createReplayAll() {
        return new ReplayRepository<>();
    }

    public static <T> ReplayRepository<T> createReplayCount(int replayCount) {
        return new ReplayRepository<>(replayCount);
    }

    private ReplayRepository() {
        mPublishSubject = PublishSubject.create();
        mObservable = mPublishSubject.replay().autoConnect();
        mObservable.subscribe();
    }

    private ReplayRepository(int replayCount) {
        mPublishSubject = PublishSubject.create();
        mObservable = mPublishSubject.replay(replayCount).autoConnect();
        mObservable.subscribe();
    }

    public void add(T value) {
        mPublishSubject.onNext(value);
    }

    public Observable<T> getObservable() {
        return mObservable;
    }
}
