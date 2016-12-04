package com.teamagam.gimelgimel.data.base.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SingleReplayRepositoryTest {

    private SingleReplayRepository<Integer> mSVR;
    private TestSubscriber<Integer> mTestSubscriber;

    @Before
    public void setUp() throws Exception {
        mSVR = new SingleReplayRepository<>();
        mTestSubscriber = new TestSubscriber<>();
    }

    @Test
    public void setValue() throws Exception {
        mSVR.setValue(1);
    }

    @Test
    public void getObservable() throws Exception {
        mSVR.getObservable();
    }

    @Test
    public void getObservable_noSetValue_shouldNotEmit() throws Exception {
        //Arrange

        //Act
        mSVR.getObservable().subscribe(mTestSubscriber);

        //Assert
        assertEmittingObservable();
        assertThat(mTestSubscriber.getOnNextEvents().size(), is(0));
    }

    @Test
    public void getObservable_setValueBefore_shouldEmitThatValue() throws Exception {
        //Arrange
        int value = 1;
        mSVR.setValue(value);

        //Act
        mSVR.getObservable().subscribe(mTestSubscriber);

        //Assert
        assertEmittingObservable();
        List<Integer> onNextEvents = mTestSubscriber.getOnNextEvents();
        assertThat(onNextEvents.size(), is(1));
        assertThat(onNextEvents.get(0), is(value));
    }

    private void assertEmittingObservable() {
        mTestSubscriber.assertNoErrors();
        mTestSubscriber.assertNotCompleted();
    }
}