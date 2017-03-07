package com.teamagam.gimelgimel.data.base.repository;

import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;

import rx.Observable;
import rx.subjects.SerializedSubject;

public class ReplayRepository<T> {

    private final SerializedSubject<T, T> mSerializedReplaySubject;

    public static <T> ReplayRepository<T> createReplayAll() {
        return new ReplayRepository<>();
    }

    public static <T> ReplayRepository<T> createReplayCount(int replayCount) {
        return new ReplayRepository<>(replayCount);
    }

    private ReplayRepository() {
        mSerializedReplaySubject = new SerializedSubjectBuilder()
                .setReplay()
                .build();
    }

    private ReplayRepository(int replayCount) {
        mSerializedReplaySubject = new SerializedSubjectBuilder()
                .setReplay()
                .setBuffer(replayCount)
                .build();
    }

    public void add(T value) {
        mSerializedReplaySubject.onNext(value);
    }

    public Observable<T> getObservable() {
        return mSerializedReplaySubject.asObservable();
    }
}
