package com.teamagam.gimelgimel.domain.messages.repository;

import com.teamagam.gimelgimel.domain.messages.entities.Message;

import java.util.List;

import rx.Observable;


/**
 * Interface that represents a Repository for getting {@link Message} related data.
 */
public interface MessagesRepository {

    Observable<List<Message>> getMessages();

    void putMessage(Message message);

    Observable<Message> sendMessage(Message message);

}

