package com.teamagam.gimelgimel.data.base.repository;

import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

public class SubjectRepository<T> {

  private final Subject<T> mSerializedReplaySubject;

  private SubjectRepository(Subject<T> subject) {
    mSerializedReplaySubject = subject;
  }

  public static <T> SubjectRepository<T> createSimpleSubject() {
    return new SubjectRepository<>(new SerializedSubjectBuilder().build());
  }

  public static <T> SubjectRepository<T> createReplayAll() {
    return new SubjectRepository<>(new SerializedSubjectBuilder().setReplay().build());
  }

  public static <T> SubjectRepository<T> createReplayCount(int replayCount) {
    return new SubjectRepository<>(
        new SerializedSubjectBuilder().setReplay().setBuffer(replayCount).build());
  }

  public void add(T value) {
    mSerializedReplaySubject.onNext(value);
  }

  public Observable<T> getObservable() {
    return mSerializedReplaySubject.hide();
  }
}
