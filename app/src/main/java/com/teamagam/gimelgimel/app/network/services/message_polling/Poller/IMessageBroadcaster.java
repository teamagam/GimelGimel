package com.teamagam.gimelgimel.app.network.services.message_polling.poller;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Message broadcasting functionality
 */
public interface IMessageBroadcaster {

    /**
     * Broadcasts message
     *
     * @param message - to broadcast
     */
    void broadcast(Message message);
}
