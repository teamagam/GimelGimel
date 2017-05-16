package com.teamagam.gimelgimel.data.message.repository.InMemory;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
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
    private final SubjectRepository<Message> mMessagesReplayRepo;
    private final SubjectRepository<Integer> mNumMessagesRepo;
    private Message mLastMessage;
    private int mNumMessages;

    @Inject
    InMemoryMessagesCache() {
        mMessagesById = new HashMap<>();

        mMessagesReplayRepo = SubjectRepository.createReplayAll();
        mNumMessagesRepo = SubjectRepository.createReplayCount(1);

        mLastMessage = null;

        mNumMessages = 0;
        mNumMessagesRepo.add(mNumMessages);
    }

    public void addMessage(Message message) {
        mMessagesById.put(message.getMessageId(), message);
        mNumMessagesRepo.add(++mNumMessages);
        mMessagesReplayRepo.add(message);
        updateLastMessage(message);
    }

    public Message getMessageById(String id) {
        if (mMessagesById.containsKey(id)) {
            return mMessagesById.get(id);
        }
        return null;
    }

    public Message getLastMessage() {
        return mLastMessage;
    }

    public Observable<Message> getMessagesObservable() {
        return mMessagesReplayRepo.getObservable();
    }

    public Observable<Integer> getNumMessagesObservable() {
        return mNumMessagesRepo.getObservable();
    }

    private void updateLastMessage(Message message) {
        if (isOlderThanLast(message)) {
            mLastMessage = message;
        }
    }

    private boolean isOlderThanLast(Message message) {
        return mLastMessage == null ||
                message.getCreatedAt().getTime() > mLastMessage.getCreatedAt().getTime();
    }
}
