package com.teamagam.gimelgimel.domain.utils;

import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.SerializedSubject;

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

    public <T> SerializedSubject<T, T> build() {
        if (mReplay) {
            if (mBuffer < 0)
                return ReplaySubject.<T>create().toSerialized();
            else
                return ReplaySubject.<T>createWithSize(mBuffer).toSerialized();
        } else {
            return PublishSubject.<T>create().toSerialized();
        }
    }
}