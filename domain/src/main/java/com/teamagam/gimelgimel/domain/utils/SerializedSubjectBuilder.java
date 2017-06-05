package com.teamagam.gimelgimel.domain.utils;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class SerializedSubjectBuilder {
  private boolean mReplay = false;
  private int mBuffer = -1;

  public SerializedSubjectBuilder setReplay() {
    mReplay = true;
    return this;
  }

  public SerializedSubjectBuilder setBuffer(int buffer) {
    mBuffer = buffer;
    return this;
  }

  public <T> Subject<T> build() {
    if (mReplay) {
      if (mBuffer < 0) {
        return ReplaySubject.<T>create().toSerialized();
      } else {
        return ReplaySubject.<T>createWithSize(mBuffer).toSerialized();
      }
    } else {
      return PublishSubject.<T>create().toSerialized();
    }
  }
}