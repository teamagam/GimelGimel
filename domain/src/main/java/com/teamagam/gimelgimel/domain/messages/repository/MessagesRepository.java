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

    Observable<Integer> getSyncNumReadObservable();

    void selectMessage(Message message);

    Observable<Message> getMessageById(String messageId);

    void markMessageRead(Message message);

    void updateNumReadMessage();

    Message getSelectedMessage();
}

