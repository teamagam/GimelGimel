package com.teamagam.gimelgimel.data.message.repository.InMemory;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * class that holds its messages in-memory
 */
@Singleton
public class InMemoryMessagesCache {

    private final Map<String, Message> mMessagesById;
    private final PublishSubject<Message> mMessagesSubject;
    private final PublishSubject<Integer> mNumMessagesSubject;
    private final Observable<Integer> mNumMessageObservable;
    private final Observable<Message> mMessagesObservable;

    private int mNumMessages;

    @Inject
    InMemoryMessagesCache() {
        mMessagesById = new HashMap<>();

        mMessagesSubject = PublishSubject.create();
        mMessagesObservable = mMessagesSubject.replay().autoConnect();
        mMessagesObservable.subscribe();

        mNumMessagesSubject = PublishSubject.create();
        mNumMessageObservable = mNumMessagesSubject.share().replay(1).autoConnect();
        mNumMessageObservable.subscribe();

        mNumMessages = 0;
        mNumMessagesSubject.onNext(0);
    }

    public void addMessage(Message message) {
        mMessagesById.put(message.getMessageId(), message);
        mNumMessagesSubject.onNext(++mNumMessages);
        mMessagesSubject.onNext(message);
    }

    public Message getMessageById(String id) {
        if (mMessagesById.containsKey(id)) {
            return mMessagesById.get(id);
        }
        return null;
    }

    public Observable<Message> getMessagesObservable() {
        return mMessagesObservable;
    }

    public Observable<Integer> getNumMessagesObservable() {
        return mNumMessageObservable;
    }
}
