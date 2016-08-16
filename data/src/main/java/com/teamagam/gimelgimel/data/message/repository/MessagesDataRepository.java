package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.repository.InMemory.InMemoryMessagesCache;
import com.teamagam.gimelgimel.data.message.repository.cloud.CloudMessagesSource;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created on 8/10/2016.
 * TODO: complete text
 */
@Singleton
public class MessagesDataRepository implements MessagesRepository {

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
    public Observable<Message> getMessages() {
        final MessageDataMapper mapper = new MessageDataMapper();
        return mSource.getMessages()
                .flatMapIterable(messages -> messages)
                .map(mapper::transform);
    }

    @Override
    public void putMessage(Message message) {
        mCache.addMessage(message);
    }

    @Override
    public Observable<Message> sendMessage(Message message) {
        // Visitor - Transform to message data
        MessageData<String> messageData = new MessageData<String>(message.getSenderId(), MessageData.TEXT);
        messageData.setContent(((MessageText) message).getText());

        final MessageDataMapper mapper = new MessageDataMapper();

        return mSource.sendMessage(messageData)
                .map(mapper::transform);
    }
}
