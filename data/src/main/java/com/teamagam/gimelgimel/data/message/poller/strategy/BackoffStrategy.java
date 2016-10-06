package com.teamagam.gimelgimel.data.message.poller.strategy;

/**
 * Defines backoff time evaluation strategy
 */
public interface BackoffStrategy {

    long getBackoffMillis();

    void reset();

    void increase();
}
