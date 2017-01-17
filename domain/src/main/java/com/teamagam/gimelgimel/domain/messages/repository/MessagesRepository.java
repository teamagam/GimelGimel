package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.util.Date;

import rx.Observable;


/**
 * Interface that represents a Repository for getting {@link com.teamagam.gimelgimel.domain.messages.entity.Message} related data.
 */
public interface MessagesRepository {

    Observable<Message> sendMessage(Message message);

    Observable<Message> getMessagesObservable();

    Observable<Message> getSelectedMessageObservable();

    Observable<Integer> getNumUnreadMessagesObservable();

    Observable<Date> getLastVisitTimestampObservable();

    Observable<Message> getMessage(String messageId);

    void putMessage(Message message);

    void selectMessage(Message message);

    void readAllUntil(Date date);
}

