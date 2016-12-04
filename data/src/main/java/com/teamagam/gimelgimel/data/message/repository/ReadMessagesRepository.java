package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.data.base.repository.SingleReplayRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class ReadMessagesRepository {

    private final ReplayRepository<Message> mReadMessageInnerRepo;
    private final SingleReplayRepository<Integer> mNumReadMessagesInnerRepo;
    private final Set<Message> mReadMessages;
    private int mNumRead;

    @Inject
    public ReadMessagesRepository() {
        mReadMessages = new HashSet<>();

        mReadMessageInnerRepo = new ReplayRepository<>();

        mNumReadMessagesInnerRepo = new SingleReplayRepository<>();

        mNumRead = 0;
        mNumReadMessagesInnerRepo.setValue(mNumRead);
    }

    public Observable<Message> getReadMessagesObservable() {
        return mReadMessageInnerRepo.getObservable();
    }

    public Observable<Integer> getNumReadMessagesObservable() {
        return mNumReadMessagesInnerRepo.getObservable();
    }

    public void read(Message message) {
        if (!isAlreadyRead(message)) {
            mReadMessages.add(message);
            mNumReadMessagesInnerRepo.setValue(++mNumRead);
            mReadMessageInnerRepo.add(message);
        }
    }

    private boolean isAlreadyRead(Message message) {
        return mReadMessages.contains(message);
    }
}
