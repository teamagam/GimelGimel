package com.gimelgimel.data.access.http;

import com.gimelgimel.domain.model.MessageModel;
import com.gimelgimel.domain.repository.MessagesRepository;

import java.util.List;

import rx.Observable;

public class MessagesRestAPI implements MessagesRepository {

    private GGMessagingAPI mRestAPI;

    public MessagesRestAPI(GGMessagingAPI api) {
        mRestAPI = api;
    }

    @Override
    public Observable<List<MessageModel>> getMessages() {
        return null;
    }

    @Override
    public Observable<MessageModel> putMessage(MessageModel messageData) {
        return null;
    }

    @Override
    public Observable<MessageModel> sendMessage(MessageModel message) {
        return null;
    }
}
