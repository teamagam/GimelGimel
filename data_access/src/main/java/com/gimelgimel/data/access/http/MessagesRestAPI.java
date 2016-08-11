package com.gimelgimel.data.access.http;

import com.gimelgimel.domain.model.MessageModel;
import com.gimelgimel.domain.repository.MessagesRepository;

public class MessagesRestAPI implements MessagesRepository {

    private RestAPI mRestAPI;

    public MessagesRestAPI() {
        mRestAPI.getMessagingAPI();
    }

    @Override
    public void sendMessage(MessageModel message) {

    }

    @Override
    public MessageModel getMessage() {
        return null;
    }
}
