package com.teamagam.gimelgimel.domain.messages.poller.strategy;

/**
 * Exponential backoff strategy implementation.
 * Time intervals grows exponentially between increases.
 */
public class ExponentialBackoffStrategy implements BackoffStrategy {

    private final int mMultiplier;
    private final int mMaxBackoffMillis;
    private final int mBaseIntervalMillis;

    private long mCurrentIntervalMillis;
    private int mCurrentBackoffMillis;

    public ExponentialBackoffStrategy(int baseIntervalMillis, int multiplier,
                                      int maxBackoffMillis) {
        mBaseIntervalMillis = baseIntervalMillis;
        mMultiplier = multiplier;
        mMaxBackoffMillis = maxBackoffMillis;

        mCurrentIntervalMillis = baseIntervalMillis;
        mCurrentBackoffMillis = 0;
    }

    @Override
    public long getBackoffMillis() {
        return Math.min(mCurrentBackoffMillis, mMaxBackoffMillis);
    }

    @Override
    public void reset() {
        mCurrentIntervalMillis = mBaseIntervalMillis;
        mCurrentBackoffMillis = 0;
    }

    @Override
    public void increase() {
        if (isBackoffLessThanMaximum()) {
            mCurrentBackoffMillis += mCurrentIntervalMillis;
            mCurrentIntervalMillis *= mMultiplier;
        }
    }

    private boolean isBackoffLessThanMaximum() {
        return mCurrentBackoffMillis < mMaxBackoffMillis;
    }
}
