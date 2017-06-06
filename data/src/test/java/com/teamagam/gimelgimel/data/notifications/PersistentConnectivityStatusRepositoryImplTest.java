package com.teamagam.gimelgimel.data.notifications;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import io.reactivex.observers.TestObserver;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class PersistentConnectivityStatusRepositoryImplTest extends BaseTest {

  private static final long TIMEOUT_MILLIS = 100;
  private static final long BUFFER = 10;
  private static final long NOT_ENOUGH = TIMEOUT_MILLIS - BUFFER;
  private static final long ENOUGH = TIMEOUT_MILLIS + BUFFER;
  private static final long MORE = ENOUGH - NOT_ENOUGH;
  private ConnectivityStatusRepository mStatusRepo;
  private TestObserver<ConnectivityStatus> mTestObserver;

  @Before
  public void setUp() {
    mStatusRepo = new PersistentConnectivityStatusRepositoryImpl(ConnectivityStatus.CONNECTED,
        TIMEOUT_MILLIS);
    mTestObserver = new TestObserver<>();
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
    mStatusRepo.getObservable().subscribe(mTestObserver);
  }

  private void assertAllAsExpected(List<ConnectivityStatus> expectedStatuses) {
    mTestObserver.assertNoErrors();
    mTestObserver.assertNotComplete();
    mTestObserver.assertValueCount(expectedStatuses.size());
    mTestObserver.assertValueSequence(expectedStatuses);
  }
}