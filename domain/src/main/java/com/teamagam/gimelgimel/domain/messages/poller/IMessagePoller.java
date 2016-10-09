package com.teamagam.gimelgimel.domain.messages.poller;

import rx.Observable;

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
    Observable poll();

    class ConnectionException extends Exception {
    }

}
