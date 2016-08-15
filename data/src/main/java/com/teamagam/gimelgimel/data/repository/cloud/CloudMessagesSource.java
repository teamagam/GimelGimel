package com.teamagam.gimelgimel.data.repository.cloud;

import com.teamagam.gimelgimel.data.network.rest.RestAPI;
import com.teamagam.gimelgimel.domain.messages.entities.Message;

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
    CloudMessagesSource(){
    }

    /**
     * Asynchronously sends message to service
     *
     * @param message - the message to send
     */
    public Observable<Message> sendMessage(final Message message) {
        return RestAPI.getInstance().getMessagingAPI().postMessage(message);
    }

}
