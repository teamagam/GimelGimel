package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

import rx.Observable;


/**
 * Interface that represents a Repository for getting {@link com.teamagam.gimelgimel.domain.messages.entity.Message} related data.
 */
public interface MessagesRepository {

    Observable<Message> getMessages();

    void putMessage(Message message);

    Observable<Message> sendMessage(Message message);

    Observable<Message> getSyncMessagesObservable();

    Observable<Message> getSyncSelectedMessageObservable();

    void selectMessage(Message message);
}

