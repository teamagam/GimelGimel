package com.teamagam.gimelgimel.data.message.repository.cloud;

import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created on 8/10/2016.
 * TODO: complete text
 */
@Singleton
public class CloudMessagesSource {

    @Inject
    GGMessagingAPI mMessagingApi;

    @Inject
    CloudMessagesSource() {
    }

    /**
     * Asynchronously sends message to service
     *
     * @param message - the message to be sent
     */
    public Observable<MessageData> sendMessage(final MessageData message) {
        return mMessagingApi.postMessage(message);
    }

    public Observable<List<MessageData>> getMessages() {
        return mMessagingApi.getMessages();
    }
}
