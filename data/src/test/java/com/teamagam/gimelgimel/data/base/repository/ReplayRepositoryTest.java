package com.teamagam.gimelgimel.data.base.repository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReplayRepositoryTest {

    private ReplayRepository<Object> mSubject;
    private TestSubscriber<Object> mTestSubscriber;

    @Before
    public void setUp() throws Exception {
        mSubject = new ReplayRepository<>();
        mTestSubscriber = new TestSubscriber<>();
    }

    @Test
    public void addObjBeforeSubscription_subscribe_expectObj() throws Exception {
        //Arrange
        Object obj1 = new Object();
        mSubject.add(obj1);

        //Act
        mSubject.getObservable().subscribe(mTestSubscriber);

        //Assert
        assertSingleItemEmitted(obj1);
    }

    @Test
    public void addObjectAfterSubscription_expectObj() throws Exception {
        //Arrange
        Object obj1 = new Object();

        //Act
        mSubject.add(obj1);
        mSubject.getObservable().subscribe(mTestSubscriber);

        //Assert
        assertSingleItemEmitted(obj1);

    }

    private void assertSingleItemEmitted(Object obj1) {
        mTestSubscriber.assertNoErrors();
        mTestSubscriber.assertNotCompleted();
        List<Object> onNextEvents = mTestSubscriber.getOnNextEvents();
        assertThat(onNextEvents.size(), is(1));
        assertThat(onNextEvents.get(0), is(obj1));
    }
}