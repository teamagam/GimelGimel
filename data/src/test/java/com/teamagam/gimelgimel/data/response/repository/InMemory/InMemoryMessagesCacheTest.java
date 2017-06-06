package com.teamagam.gimelgimel.data.response.repository.InMemory;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import io.reactivex.observers.TestObserver;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class InMemoryMessagesCacheTest extends BaseTest {

  InMemoryMessagesCache mCache;

  @Before
  public void setUp() throws Exception {
    mCache = new InMemoryMessagesCache();
  }

  @Test
  public void getMessagesObservable_onLateSubscription_shouldGetAllAddedMessages()
      throws Exception {
    //Arrange
    ChatMessage m = mock(ChatMessage.class);
    TestObserver<ChatMessage> testObserver = new TestObserver<>();

    //Act
    mCache.addMessage(m);
    mCache.getMessagesObservable().subscribe(testObserver);

    //Assert
    testObserver.assertNoErrors();
    testObserver.assertNotComplete();
    testObserver.assertValue(m);
  }

  @Test
  public void getMessageObservable_onEarlySubscription_shouldGetAllAddedMessages()
      throws Exception {
    //Arrange
    ChatMessage m = mock(ChatMessage.class);
    TestObserver<ChatMessage> testObserver = new TestObserver<>();

    //Act
    mCache.getMessagesObservable().subscribe(testObserver);
    mCache.addMessage(m);

    //Assert
    testObserver.assertNoErrors();
    testObserver.assertNotComplete();
    testObserver.assertValue(m);
  }

  @Test
  public void addMessage() throws Exception {

  }

  @Test
  public void getMessageById() throws Exception {

  }

  @Test
  public void getMessagesObservable() throws Exception {

  }

  @Test
  public void getNumMessagesObservable_onZeroAdds_shouldEmitZero() throws Exception {
    //Arrange
    TestObserver<Integer> testObserver = new TestObserver<>();

    //Act
    mCache.getNumMessagesObservable().subscribe(testObserver);

    //Assert
    testObserver.assertNoErrors();
    testObserver.assertNotComplete();
    testObserver.assertValue(0);
  }

  @Test
  public void getNumMessagesObservable_onOneAddBeforeSubscribing_shouldEmitOne() throws Exception {
    //Arrange
    TestObserver<Integer> testObserver = new TestObserver<>();

    //Act
    mCache.addMessage(mock(ChatMessage.class));
    mCache.getNumMessagesObservable().subscribe(testObserver);

    //Assert
    testObserver.assertNoErrors();
    testObserver.assertNotComplete();
    testObserver.assertValue(1);
  }
}