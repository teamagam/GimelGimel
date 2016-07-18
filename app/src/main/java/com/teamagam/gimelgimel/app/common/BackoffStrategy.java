package com.teamagam.gimelgimel.app.common;

/**
 * Defines backoff time evaluation strategy
 */
public interface BackoffStrategy {

    long getBackoffMillis();

    void reset();

    void increase();
}
