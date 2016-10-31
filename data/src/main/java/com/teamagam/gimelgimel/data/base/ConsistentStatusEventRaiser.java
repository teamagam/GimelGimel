package com.teamagam.gimelgimel.data.base;

import android.os.CountDownTimer;

/**
 * @param <T> A status object, must implement equals
 */
public class ConsistentStatusEventRaiser<T> {

    private boolean mIsCountDownStarted;
    private long mConsistentMilliseconds;
    private T mCurrentConsistentStatus;
    private T mTempStatus;
    private ConsistentCountDownTimer mConsistentCountDownTimer;
    private EventAction<T> mEventAction;

    public ConsistentStatusEventRaiser(long consistentMilliseconds, T currentStatus,
                                       EventAction<T> eventAction) {
        mCurrentConsistentStatus = currentStatus;
        mConsistentMilliseconds = consistentMilliseconds;
        mEventAction = eventAction;
        mConsistentCountDownTimer =
                new ConsistentCountDownTimer(mConsistentMilliseconds, mConsistentMilliseconds);

        mEventAction.Do(currentStatus);
    }

    public void updateStatus(T newStatus) {
        if (!isConsistentUpdate(newStatus) && mIsCountDownStarted) {
            mTempStatus = null;
            mIsCountDownStarted = false;
            mConsistentCountDownTimer.cancel();
        } else if (isConsistentUpdate(newStatus)) {
            mTempStatus = newStatus;
            mIsCountDownStarted = true;
            mConsistentCountDownTimer.start();
        }
    }

    private boolean isConsistentUpdate(T statusToCompare) {
        return !statusToCompare.equals(mCurrentConsistentStatus) &&
                (mTempStatus == null || statusToCompare.equals(mTempStatus));
    }

    public interface EventAction<T> {
        void Do(T consistentStatus);
    }

    public class ConsistentCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public ConsistentCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (mTempStatus != null) {
                mCurrentConsistentStatus = mTempStatus;
                mEventAction.Do(mCurrentConsistentStatus);
            }

            mTempStatus = null;
            mIsCountDownStarted = false;
        }
    }
}
