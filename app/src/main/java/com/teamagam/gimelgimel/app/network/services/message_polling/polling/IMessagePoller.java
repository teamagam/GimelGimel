package com.teamagam.gimelgimel.app.network.services.message_polling.polling;

/**
 * Defines poller functionality for use across the system.
 * The polling term means establishing a connection with a remote resource to ask for new data.
 * A poller needs to handle new data.
 */
public interface IMessagePoller {

    /**
     * Polls server for new messages
     *
     * @throws ConnectionException - thrown on connectivity error with the server
     */
    void poll() throws ConnectionException;

    class ConnectionException extends Exception {
    }
}
