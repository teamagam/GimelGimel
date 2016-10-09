package com.teamagam.gimelgimel.domain.messages.poller.strategy;

/**
 * Defines backoff time evaluation strategy
 */
public interface BackoffStrategy {

    long getBackoffMillis();

    void reset();

    void increase();
}
