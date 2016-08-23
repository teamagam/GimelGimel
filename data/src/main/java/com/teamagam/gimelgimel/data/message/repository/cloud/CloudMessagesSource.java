package com.teamagam.gimelgimel.data.message.repository.cloud;

import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.rest.RestAPI;

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

    // TODO: Add to the members of this class and fix dagger
    //GGMessagingAPI mMessagingApi;

    @Inject
    CloudMessagesSource() {
    }

    /**
     * Asynchronously sends message to service
     *
     * @param message - the message to send
     */
    public Observable<MessageData> sendMessage(final MessageData message) {
        return new RestAPI().getMessagingAPI().postMessage(message);
    }

    public Observable<List<MessageData>> getMessages() {
        return new RestAPI().getMessagingAPI().getMessages();
    }
}
