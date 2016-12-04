package com.teamagam.gimelgimel.data.message.repository.InMemory;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.data.base.repository.SingleReplayRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * class that holds its messages in-memory
 */
@Singleton
public class InMemoryMessagesCache {

    private final Map<String, Message> mMessagesById;
    private final ReplayRepository<Message> mMessagesReplayRepo;
    private final SingleReplayRepository<Integer> mNumMessagesRepo;

    private int mNumMessages;

    @Inject
    InMemoryMessagesCache() {
        mMessagesById = new HashMap<>();

        mMessagesReplayRepo = new ReplayRepository<>();

        mNumMessagesRepo = new SingleReplayRepository<>();

        mNumMessages = 0;
        mNumMessagesRepo.setValue(mNumMessages);
    }

    public void addMessage(Message message) {
        mMessagesById.put(message.getMessageId(), message);
        mNumMessagesRepo.setValue(++mNumMessages);
        mMessagesReplayRepo.add(message);
    }

    public Message getMessageById(String id) {
        if (mMessagesById.containsKey(id)) {
            return mMessagesById.get(id);
        }
        return null;
    }

    public Observable<Message> getMessagesObservable() {
        return mMessagesReplayRepo.getObservable();
    }

    public Observable<Integer> getNumMessagesObservable() {
        return mNumMessagesRepo.getObservable();
    }
}
