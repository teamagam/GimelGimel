package com.teamagam.gimelgimel.app.common;

import com.teamagam.gimelgimel.app.common.data.ExponentialBackoffStrategy;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ExponentialBackoffStrategyTest {

    private static final int BASE_INTERVAL_MILLIS = 10;
    private static final int MULTIPLIER = 2;
    private static final int MAX_BACKOFF_MILLIS = 100;
    private ExponentialBackoffStrategy mExponentialBackoffStratgey;

    @Before
    public void setUp() throws Exception {
        mExponentialBackoffStratgey = new ExponentialBackoffStrategy(
                BASE_INTERVAL_MILLIS, MULTIPLIER, MAX_BACKOFF_MILLIS);
    }

    @Test
    public void getBackoffMillisAfterCreation_shouldReturnZero() throws Exception {
        long res = mExponentialBackoffStratgey.getBackoffMillis();

        assertThat(res, equalTo(0L));
    }

    @Test
    public void afterOneIncrease_shouldReturnBaseInterval() throws Exception {
        mExponentialBackoffStratgey.increase();

        assertThat(mExponentialBackoffStratgey.getBackoffMillis(),
                Matchers.equalTo((long) BASE_INTERVAL_MILLIS));
    }

    @Test
    public void afterTwoIncreases_shouldReturnBasePlusBaseTimesMultiplierBackoff() throws Exception {
        mExponentialBackoffStratgey.increase();
        mExponentialBackoffStratgey.increase();

        assertThat(mExponentialBackoffStratgey.getBackoffMillis(),
                equalTo((long) (BASE_INTERVAL_MILLIS + BASE_INTERVAL_MILLIS * MULTIPLIER)));
    }

    @Test
    public void afterIncreasesPassMax_shouldReturnMax() throws Exception {
        mExponentialBackoffStratgey.increase();
        mExponentialBackoffStratgey.increase();
        mExponentialBackoffStratgey.increase();
        mExponentialBackoffStratgey.increase();
        mExponentialBackoffStratgey.increase();

        assertThat(mExponentialBackoffStratgey.getBackoffMillis(),
                equalTo((long) MAX_BACKOFF_MILLIS));
    }

    @Test
    public void resetAfterIncreases_shouldReturnZero() throws Exception {
        mExponentialBackoffStratgey.increase();
        mExponentialBackoffStratgey.increase();
        mExponentialBackoffStratgey.increase();

        mExponentialBackoffStratgey.reset();

        assertThat(mExponentialBackoffStratgey.getBackoffMillis(), equalTo(0L));
    }

    @Test
    public void increaseResetIncrease_shouldReturnBaseInterval() throws Exception {
        mExponentialBackoffStratgey.increase();
        mExponentialBackoffStratgey.reset();
        mExponentialBackoffStratgey.increase();

        assertThat(mExponentialBackoffStratgey.getBackoffMillis(),
                equalTo((long) BASE_INTERVAL_MILLIS));
    }
}