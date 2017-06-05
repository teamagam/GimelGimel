package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import io.reactivex.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PersistentConnectivityStatusRepositoryImplTest extends BaseTest {

  private static final long TIMEOUT_MILLIS = 100;
  private static final long BUFFER = 10;
  private static final long NOT_ENOUGH = TIMEOUT_MILLIS - BUFFER;
  private static final long ENOUGH = TIMEOUT_MILLIS + BUFFER;
  private static final long MORE = ENOUGH - NOT_ENOUGH;
  private ConnectivityStatusRepository mStatusRepo;
  private TestSubscriber<ConnectivityStatus> mTestSubscriber;
  private List<ConnectivityStatus> mActualEmittedStatuses;

  @Before
  public void setUp() {
    mStatusRepo = new PersistentConnectivityStatusRepositoryImpl(ConnectivityStatus.CONNECTED,
        TIMEOUT_MILLIS);
    mTestSubscriber = new TestSubscriber<>();
    subscribe();
  }

  @Test
  public void whenInitiatedWithConnected_ThenConnectedEmitted() {
    //Assert
    assertAllAsExpected(Collections.singletonList(ConnectivityStatus.CONNECTED));
  }

  @Test
  public void whenSetConnected_ThenNoChange() {
    //Act
    mStatusRepo.setStatus(ConnectivityStatus.CONNECTED);

    //Assert
    assertAllAsExpected(Collections.singletonList(ConnectivityStatus.CONNECTED));
  }

  @Test
  public void whenSetDisconnected_ThenNoImmediateChange() {
    //Act
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);

    //Assert
    assertAllAsExpected(Collections.singletonList(ConnectivityStatus.CONNECTED));
  }

  @Test
  public void whenSetDisconnected_ThenDisconnectedEmittedAfterTimeout()
      throws InterruptedException {
    //Act
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    Thread.sleep(ENOUGH);

    //Assert
    assertAllAsExpected(
        Arrays.asList(ConnectivityStatus.CONNECTED, ConnectivityStatus.DISCONNECTED));
  }

  @Test
  public void whenTemporaryDisconnected_ThenNoChange() throws InterruptedException {
    //Act
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    Thread.sleep(NOT_ENOUGH);
    mStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
    Thread.sleep(MORE);

    //Assert
    assertAllAsExpected(Collections.singletonList(ConnectivityStatus.CONNECTED));
  }

  @Test
  public void instrumentationTest() throws InterruptedException {
    //Act
    mStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
    Thread.sleep(ENOUGH);
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    Thread.sleep(ENOUGH);
    mStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
    mStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
    mStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
    Thread.sleep(ENOUGH);
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    Thread.sleep(MORE);
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    Thread.sleep(MORE);
    mStatusRepo.setStatus(ConnectivityStatus.CONNECTED);
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    Thread.sleep(ENOUGH);
    mStatusRepo.setStatus(ConnectivityStatus.DISCONNECTED);
    Thread.sleep(ENOUGH);

    //Assert
    assertAllAsExpected(Arrays.asList(ConnectivityStatus.CONNECTED, ConnectivityStatus.DISCONNECTED,
        ConnectivityStatus.CONNECTED, ConnectivityStatus.DISCONNECTED));
  }

  private void subscribe() {
    mStatusRepo.getObservable().subscribe(mTestSubscriber);
  }

  private void assertAllAsExpected(List<ConnectivityStatus> expectedStatuses) {
    mActualEmittedStatuses = mTestSubscriber.getOnNextEvents();
    assertObservableOpen();
    assertNumberOfEmittedItems(expectedStatuses.size());
    assertStatusList(expectedStatuses);
  }

  private void assertObservableOpen() {
    mTestSubscriber.assertNoErrors();
    mTestSubscriber.assertNotCompleted();
  }

  private void assertNumberOfEmittedItems(int num) {
    assertThat(mActualEmittedStatuses.size(), is(num));
  }

  private void assertStatusList(List<ConnectivityStatus> expectedEvents) {
    for (int i = 0; i < mActualEmittedStatuses.size(); i++) {
      ConnectivityStatus status = mActualEmittedStatuses.get(i);
      assertThat(status, is(expectedEvents.get(i)));
    }
  }
}