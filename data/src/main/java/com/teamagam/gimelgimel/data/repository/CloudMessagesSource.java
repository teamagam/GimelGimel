package com.teamagam.gimelgimel.data.repository;

import com.teamagam.gimelgimel.data.network.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.network.rest.RestAPI;
import com.teamagam.gimelgimel.domain.messages.entities.Message;

import rx.Observable;

/**
 * Created on 8/10/2016.
 * TODO: complete text
 */
public class CloudMessagesSource {

    /**
     * Asynchronously sends message to service
     *
     * @param message - the message to send
     */
    public Observable<Message> sendMessage(final Message message) {
        GGMessagingAPI m = RestAPI.getInstance().getMessagingAPI();
        return m.postMessage(message);
    }

}
