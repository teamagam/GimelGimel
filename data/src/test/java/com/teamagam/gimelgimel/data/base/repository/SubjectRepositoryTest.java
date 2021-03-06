package com.teamagam.gimelgimel.data.base.repository;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import io.reactivex.observers.TestObserver;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

public class SubjectRepositoryTest extends BaseTest {

  private SubjectRepository<Object> mSubject;
  private TestObserver<Object> mTestObserver;

  @Before
  public void setUp() throws Exception {
    mSubject = SubjectRepository.createReplayAll();
    mTestObserver = new TestObserver<>();
  }

  @Test
  public void addObjBeforeSubscription_subscribe_expectObj() throws Exception {
    //Arrange
    Object obj1 = new Object();
    mSubject.add(obj1);

    //Act
    mSubject.getObservable().subscribe(mTestObserver);

    //Assert
    assertSingleItemEmitted(obj1);
  }

  @Test
  public void addObjectAfterSubscription_expectObj() throws Exception {
    //Arrange
    Object obj1 = new Object();

    //Act
    mSubject.getObservable().subscribe(mTestObserver);
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
    testSubject.getObservable().subscribe(mTestObserver);

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
    testSubject.getObservable().subscribe(mTestObserver);
    testSubject.add(obj2);

    //Assert
    assertObservableOpen();
    mTestObserver.assertValueSequence(Arrays.asList(obj1, obj2));
  }

  private void assertSingleItemEmitted(Object obj1) {
    assertObservableOpen();
    mTestObserver.assertValueSequence(Collections.singletonList(obj1));
  }

  private void assertObservableOpen() {
    mTestObserver.assertNoErrors();
    mTestObserver.assertNotComplete();
  }
}