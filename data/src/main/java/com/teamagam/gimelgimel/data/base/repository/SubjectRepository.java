package com.teamagam.gimelgimel.data.base.repository;

import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;

import rx.Observable;
import rx.subjects.SerializedSubject;

public class SubjectRepository<T> {

    private final SerializedSubject<T, T> mSerializedReplaySubject;

    public static <T> SubjectRepository<T> createSimpleSubject() {
        return new SubjectRepository<>(new SerializedSubjectBuilder().build());
    }

    public static <T> SubjectRepository<T> createReplayAll() {
        return new SubjectRepository<>(new SerializedSubjectBuilder()
                .setReplay()
                .build());
    }

    public static <T> SubjectRepository<T> createReplayCount(int replayCount) {
        return new SubjectRepository<>(new SerializedSubjectBuilder()
                .setReplay()
                .setBuffer(replayCount)
                .build());
    }

    private SubjectRepository(SerializedSubject subject) {
        mSerializedReplaySubject = subject;
    }

    public void add(T value) {
        mSerializedReplaySubject.onNext(value);
    }

    public Observable<T> getObservable() {
        return mSerializedReplaySubject.asObservable();
    }
}
