package com.teamagam.gimelgimel.domain.utils;

import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.SerializedSubject;

public class SerializedSubjectBuilder {
    private boolean replay = false;
    private int buffer = -1;

    public SerializedSubjectBuilder setReplay() {
        replay = true;
        return this;
    }

    public SerializedSubjectBuilder setBuffer(int buffer) {
        this.buffer = buffer;
        return this;
    }

    public <T> SerializedSubject<T, T> build() {
        if(replay && buffer < 0) {
            return ReplaySubject.<T>create().toSerialized();
        } else if(replay) {
            return ReplaySubject.<T>createWithSize(buffer).toSerialized();
        } else {
            return PublishSubject.<T>create().toSerialized();
        }
    }
}