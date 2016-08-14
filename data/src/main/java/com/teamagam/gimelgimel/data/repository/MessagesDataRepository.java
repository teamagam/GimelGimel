package com.teamagam.gimelgimel.data.repository;

import com.teamagam.gimelgimel.data.repository.InMemory.InMemoryMessagesCache;
import com.teamagam.gimelgimel.data.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.domain.messages.entities.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created on 8/10/2016.
 * TODO: complete text
 */
@Singleton
public class MessagesDataRepository implements MessagesRepository{

    @Inject
    CloudMessagesSource mSource;

    @Inject
    InMemoryMessagesCache mCache;

    @Inject
    public MessagesDataRepository(CloudMessagesSource source, InMemoryMessagesCache cache) {
        mSource = source;
        mCache = cache;
    }

    @Override
    public Observable<List<Message>> getMessages() {
        return null;
    }

    @Override
    public void putMessage(Message message) {
        mCache.addMessage(message);
    }

    @Override
    public Observable<Message> sendMessage(Message message) {
        return mSource.sendMessage(message);
    }
}
