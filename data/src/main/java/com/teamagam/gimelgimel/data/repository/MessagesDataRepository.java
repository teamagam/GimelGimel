package com.teamagam.gimelgimel.data.repository;

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

    private final CloudMessagesSource source;

    @Inject
    public MessagesDataRepository() {
        source = new CloudMessagesSource();
    }

    @Override
    public Observable<List<Message>> getMessages() {
        return null;
    }

    @Override
    public Observable<Message> putMessage(Message message) {
        return null;
    }

    @Override
    public Observable<Message> sendMessage(Message message) {
        return source.sendMessage(message);
    }
}
