package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

import rx.Observable;


/**
 * Interface that represents a Repository for getting {@link com.teamagam.gimelgimel.domain.messages.entity.Message} related data.
 */
public interface MessagesRepository {

    Observable<Message> sendMessage(Message message);

    Observable<Message> getMessagesObservable();

    Observable<Message> getSelectedMessageObservable();

    Observable<Message> getReadMessagesObservable();

    Observable<Integer> getNumUnreadMessagesObservable();

    Observable<Message> getMessage(String messageId);

    void putMessage(Message message);

    void selectMessage(Message message);

    void markMessageRead(Message message);
}

