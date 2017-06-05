package com.teamagam.gimelgimel.data.base.repository;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import io.reactivex.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SubjectRepositoryTest extends BaseTest {

  private SubjectRepository<Object> mSubject;
  private TestSubscriber<Object> mTestSubscriber;

  @Before
  public void setUp() throws Exception {
    mSubject = SubjectRepository.createReplayAll();
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
    mSubject.getObservable().subscribe(mTestSubscriber);
    mSubject.add(obj1);

    //Assert
    assertSingleItemEmitted(obj1);
  }

  @Test
  public void addTwoObjectsBeforeSubscription_subscribeWithReplayOne_expectLast() throws Exception {
    //Arrange
    SubjectRepository<Object> testSubject = SubjectRepository.createReplayCount(1);
    testSubject.add(new Object());
    Object obj1 = new Object();
    testSubject.add(obj1);

    //Act
    testSubject.getObservable().subscribe(mTestSubscriber);

    //Assert
    assertSingleItemEmitted(obj1);
  }

  @Test
  public void addObjBeforeSubscriptionAndAfter_subscribeSingleReplayBetween_expectBoth()
      throws Exception {
    //Arrange
    SubjectRepository<Object> testSubject = SubjectRepository.createReplayCount(1);
    Object obj1 = new Object();
    Object obj2 = new Object();
    testSubject.add(obj1);

    //Act
    testSubject.getObservable().subscribe(mTestSubscriber);
    testSubject.add(obj2);

    //Assert
    assertObservableOpen();
    List<Object> onNextEvents = mTestSubscriber.getOnNextEvents();
    assertThat(onNextEvents.size(), is(2));
    assertThat(onNextEvents.get(0), is(obj1));
    assertThat(onNextEvents.get(1), is(obj2));
  }

  private void assertSingleItemEmitted(Object obj1) {
    assertObservableOpen();
    List<Object> onNextEvents = mTestSubscriber.getOnNextEvents();
    assertThat(onNextEvents.size(), is(1));
    assertThat(onNextEvents.get(0), is(obj1));
  }

  private void assertObservableOpen() {
    mTestSubscriber.assertNoErrors();
    mTestSubscriber.assertNotCompleted();
  }
}