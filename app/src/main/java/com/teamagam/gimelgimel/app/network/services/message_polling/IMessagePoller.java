package com.teamagam.gimelgimel.app.network.services.message_polling;

/**
 * Defines poller functionality for use across the system.
 * The polling term means establishing a connection with a remote resource to ask for new data.
 * A poller needs to handle new data.
 */
public interface IMessagePoller {

    /**
     * Polls for new messages from the GG-system and handles them
     */
    void poll();
}
