package com.gimelgimel.domain.repository;

import com.gimelgimel.domain.model.MessageModel;

import java.util.List;

import rx.Observable;

public interface MessagesRepository {
    Observable<List<MessageModel>> getMessages();

    Observable<MessageModel> putMessage(MessageModel messageData);

    Observable<MessageModel> sendMessage(MessageModel message);
}
