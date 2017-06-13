package com.teamagam.gimelgimel.domain.messages.poller;

import io.reactivex.Flowable;

/**
 * Defines poller functionality for use across the system.
 * The polling term means establishing a connection with a remote resource to ask for new data.
 * A poller needs to handle new data.
 */
public interface IMessagePoller {

  Flowable poll();
}
