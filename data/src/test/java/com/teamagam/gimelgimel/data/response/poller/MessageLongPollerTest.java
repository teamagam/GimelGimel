package com.teamagam.gimelgimel.data.response.poller;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.response.adapters.ServerDataMapper;
import com.teamagam.gimelgimel.data.response.entity.ServerResponse;
import com.teamagam.gimelgimel.data.response.entity.TextMessageResponse;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;
import com.teamagam.gimelgimel.data.response.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.response.rest.exceptions.RetrofitException;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.poller.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessageLongPollerTest extends BaseTest {

  private MessageLongPoller mMessagePoller;
  private GGMessagingAPI mGGMessagingAPIMock;
  private IPolledMessagesProcessor mPolledMessagesProcessorMock;
  private UserPreferencesRepository mPreferenceProviderMock;

  private TestSubscriber<Long> mTestSubscriber;

  @Before
  public void setUp() throws Exception {

    mGGMessagingAPIMock = mock(GGMessagingAPI.class);
    mPolledMessagesProcessorMock = mock(IPolledMessagesProcessor.class);
    ServerDataMapper serverDataMapper = mock(ServerDataMapper.class);
    mMessagePoller =
        new MessageLongPoller(mGGMessagingAPIMock, serverDataMapper, mPolledMessagesProcessorMock);

    doAnswer(invocation -> {
      Object[] args = invocation.getArguments();
      ServerResponse response = (ServerResponse) args[0];
      ChatMessage m = mock(ChatMessage.class);
      Date createdAt = response.getCreatedAt();
      when(m.getCreatedAt()).thenReturn(createdAt);
      return m;
    }).when(serverDataMapper).transform(any(ServerResponse.class));

    mTestSubscriber = new TestSubscriber<>();

    mPreferenceProviderMock = mock(UserPreferencesRepository.class);
    mMessagePoller.mPrefs = mPreferenceProviderMock;
  }

  @Test
  public void testPoll_onUnknownRuntimeException_shouldThrowRuntimeException() throws Exception {
    //Arrange
    when(mGGMessagingAPIMock.getMessagesFromDate(anyLong())).thenReturn(
        Observable.error(new RuntimeException()));
    when(mPreferenceProviderMock.getLong(anyString())).thenReturn((long) 0);

    //Act
    mMessagePoller.poll().subscribe(mTestSubscriber);

    //Assert
    mTestSubscriber.assertError(Exception.class);
  }

  @Test
  public void testPoll_onConnectionTimeout_shouldReturnOriginalSyncDate() throws Exception {
    //Arrange
    long syncDate = 123;
    RetrofitException re = mock(RetrofitException.class);
    when(re.getCause()).thenReturn(new SocketTimeoutException());
    when(mGGMessagingAPIMock.getMessagesFromDate(anyLong())).thenReturn(Observable.error(re));
    when(mPreferenceProviderMock.getLong(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(syncDate);

    //Act
    mMessagePoller.poll().subscribe(mTestSubscriber);

    //Assert
    mTestSubscriber.assertNoErrors();
    mTestSubscriber.assertCompleted();
    List<Long> onNextEvents = mTestSubscriber.getOnNextEvents();
    assertThat(onNextEvents.size(), is(1));
    assertThat(onNextEvents.get(0), is(syncDate));
  }

  @Test
  public void testPoll_noMessages_synchronizationDateStaysTheSame() throws Exception {
    //Arrange
    long syncDate = 123;
    when(mGGMessagingAPIMock.getMessagesFromDate(syncDate)).thenReturn(
        Observable.just(new ArrayList<>()));
    when(mPreferenceProviderMock.getLong(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(syncDate);

    //Act
    mMessagePoller.poll();

    //Assert
    verify(mPreferenceProviderMock, never()).setPreference(eq(Constants.LATEST_MESSAGE_DATE_KEY),
        anyLong());
  }

  @Test
  public void testPoll_noMessages_shouldntProcessAnything() throws Exception {
    //Arrange
    long syncDate = 123;
    when(mGGMessagingAPIMock.getMessagesFromDate(syncDate)).thenReturn(
        Observable.just(new ArrayList<>()));
    when(mPreferenceProviderMock.getLong(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(syncDate);

    //Act
    mMessagePoller.poll();

    //Assert
    verify(mPolledMessagesProcessorMock, never()).process(any(ChatMessage.class));
  }

  @Test
  public void testPoll_polledMessagesNotEmpty_shouldUpdateSynchronizationToMaxMessageDate()
      throws Exception {
    //Arrange
    long syncDate = 123;
    when(mPreferenceProviderMock.getLong(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(syncDate);

    ServerResponse messageMock1 = mock(ServerResponse.class);
    ServerResponse messageMock2 = mock(ServerResponse.class);
    ServerResponse messageMock3 = mock(ServerResponse.class);
    Date date1 = new Date();
    Date date2 = new Date(date1.getTime() + 1000);
    Date date3 = new Date(date2.getTime() + 1000);
    when(messageMock1.getCreatedAt()).thenReturn(date1);
    when(messageMock2.getCreatedAt()).thenReturn(date2);
    when(messageMock3.getCreatedAt()).thenReturn(date3);
    List<ServerResponse> apiMessages = Arrays.asList(messageMock1, messageMock2, messageMock3);

    when(mGGMessagingAPIMock.getMessagesFromDate(syncDate)).thenReturn(
        Observable.just(apiMessages));

    //Act
    mMessagePoller.poll().subscribe(mTestSubscriber);

    //Assert
    mTestSubscriber.assertNoErrors();
    mTestSubscriber.assertCompleted();
    List<Long> onNextEvents = mTestSubscriber.getOnNextEvents();
    assertThat(onNextEvents.size(), is(1));
    assertThat(onNextEvents.get(0), is(date3.getTime()));
  }

  @Test
  public void testPoll_polledMessagesNotEmpty_shouldProcessAll() throws Exception {
    //Arrange
    long syncDate = 123;
    when(mPreferenceProviderMock.getLong(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(syncDate);

    TextMessageResponse messageMock1 = mock(TextMessageResponse.class);
    Date date1 = new Date();
    when(messageMock1.getCreatedAt()).thenReturn(date1);
    List<ServerResponse> apiMessages = Arrays.asList(messageMock1);

    when(mGGMessagingAPIMock.getMessagesFromDate(syncDate)).thenReturn(
        Observable.just(apiMessages));
    doAnswer(invocation -> {
      ResponseVisitor visitor = invocation.getArgument(0);
      visitor.visit(((TextMessageResponse) invocation.getMock()));

      return null;
    }).when(messageMock1).accept(any(ResponseVisitor.class));

    //Act
    mMessagePoller.poll().subscribe();

    //Assert
    verify(mPolledMessagesProcessorMock, times(1)).process(any(ChatMessage.class));
  }
}