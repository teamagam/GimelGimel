package com.teamagam.gimelgimel.app.network.services.message_polling;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * MEssage broadcasting functionality
 */
public interface IMessageBroadcaster {

    /**
     * Locally broadcasts message
     * @param message - to broadcast
     */
    void broadcast(Message message);
}
