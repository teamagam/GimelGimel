package com.teamagam.gimelgimel.app.network.services.message_polling.polling;

import com.teamagam.gimelgimel.app.message.model.MessageApp;

/**
 * MessageApp broadcasting functionality
 */
public interface IMessageBroadcaster {

    /**
     * Broadcasts message
     *
     * @param message - to broadcast
     */
    void broadcast(MessageApp message);
}
