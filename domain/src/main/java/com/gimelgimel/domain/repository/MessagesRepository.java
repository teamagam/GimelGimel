package com.gimelgimel.domain.repository;

import com.gimelgimel.domain.model.MessageModel;

import rx.Observable;

public interface MessagesRepository {
    Observable<MessageModel> getMessages();

    Observable<MessageModel> putMessage(MessageModel messageData);

    Observable<MessageModel> sendMessage(MessageModel message);
}
