package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.base.subscribers.RxUtils;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

@Singleton
public class ReadMessagesRepository {

    private final PublishSubject<Message> mReadMessagesSubject;
    private final PublishSubject<Integer> mNumReadSubject;
    private final Set<Message> mReadMessages;
    private final Observable<Message> mReadMessagesObservable;
    private final Observable<Integer> mNumReadObservable;
    private int mNumRead;

    @Inject
    public ReadMessagesRepository() {
        mReadMessages = new HashSet<>();

        mReadMessagesSubject = PublishSubject.create();
        mReadMessagesObservable = RxUtils.getReplayObservable(mReadMessagesSubject);

        mNumReadSubject = PublishSubject.create();
        mNumReadObservable = RxUtils.getReplayObservable(mNumReadSubject, 1);

        mNumRead = 0;
        mNumReadSubject.onNext(mNumRead);
    }

    public Observable<Message> getReadMessagesObservable() {
        return mReadMessagesObservable;
    }

    public Observable<Integer> getNumReadMessagesObservable() {
        return mNumReadObservable;
    }

    public void read(Message message) {
        if (!isAlreadyRead(message)) {
            mReadMessages.add(message);
            mNumReadSubject.onNext(++mNumRead);
            mReadMessagesSubject.onNext(message);
        }
    }

    private boolean isAlreadyRead(Message message) {
        return mReadMessages.contains(message);
    }
}