package com.teamagam.gimelgimel.data.message.poller;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.entity.MessageData;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.message.rest.exceptions.RetrofitException;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.poller.IMessagePoller;
import com.teamagam.gimelgimel.domain.messages.poller.IPolledMessagesProcessor;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import org.junit.Before;
import org.junit.Test;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
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

public class MessageLongPollerTest {

    private MessageLongPoller mMessagePoller;
    private GGMessagingAPI mGGMessagingAPIMock;
    private IPolledMessagesProcessor mPolledMessagesProcessorMock;
    private UserPreferencesRepository mPreferenceProviderMock;

    private TestSubscriber<Long> mTestSubscriber;

    @Before
    public void setUp() throws Exception {

        mGGMessagingAPIMock = mock(GGMessagingAPI.class);
        mPolledMessagesProcessorMock = mock(IPolledMessagesProcessor.class);
        MessageDataMapper messageDataMapper = mock(MessageDataMapper.class);
        mMessagePoller = new MessageLongPoller(mGGMessagingAPIMock, messageDataMapper,
                mPolledMessagesProcessorMock);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Collection<MessageData> messageDatas = (Collection<MessageData>) args[0];
            List<Message> res = new ArrayList<>();
            for (MessageData msdata : messageDatas) {
                Message m = mock(Message.class);
                Date createdAt = msdata.getCreatedAt();
                when(m.getCreatedAt()).thenReturn(createdAt);
                res.add(m);
            }
            return res;
        }).when(messageDataMapper).transform(anyCollection());

        mTestSubscriber = new TestSubscriber<>();

        mPreferenceProviderMock = mock(UserPreferencesRepository.class);
        mMessagePoller.mPrefs = mPreferenceProviderMock;
    }

    @Test
    public void testPoll_onUnknownRuntimeException_shouldThrowRuntimeException() throws Exception {
        //Arrange
        when(mGGMessagingAPIMock.getMessagesFromDate(anyLong())).thenReturn(
                Observable.error(new RuntimeException()));
        when(mPreferenceProviderMock.getPreference(anyString())).thenReturn(0l);

        //Act
        mMessagePoller.poll().subscribe(mTestSubscriber);

        //Assert
        mTestSubscriber.assertError(IMessagePoller.ConnectionException.class);
    }

    @Test
    public void testPoll_onConnectionTimeout_shouldReturnOriginalSyncDate() throws Exception {
        //Arrange
        long syncDate = 123;
        RetrofitException re = mock(RetrofitException.class);
        when(re.getCause()).thenReturn(new SocketTimeoutException());
        when(mGGMessagingAPIMock.getMessagesFromDate(anyLong())).thenReturn(Observable.error(re));
        when(mPreferenceProviderMock.getPreference(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(
                syncDate);

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
        when(mPreferenceProviderMock.getPreference(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(
                syncDate);

        //Act
        mMessagePoller.poll();

        //Assert
        verify(mPreferenceProviderMock, never()).setPreference(
                eq(Constants.LATEST_MESSAGE_DATE_KEY), anyLong());
    }

    @Test
    public void testPoll_noMessages_shouldntProcessAnything() throws Exception {
        //Arrange
        long syncDate = 123;
        when(mGGMessagingAPIMock.getMessagesFromDate(syncDate)).thenReturn(
                Observable.just(new ArrayList<>()));
        when(mPreferenceProviderMock.getPreference(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(
                syncDate);

        //Act
        mMessagePoller.poll();

        //Assert
        verify(mPolledMessagesProcessorMock, never()).process(any(Collection.class));
    }

    @Test
    public void testPoll_polledMessagesNotEmpty_shouldUpdateSynchronizationToMaxMessageDate() throws Exception {
        //Arrange
        long syncDate = 123;
        when(mPreferenceProviderMock.getPreference(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(
                syncDate);

        MessageData messageMock1 = mock(MessageData.class);
        MessageData messageMock2 = mock(MessageData.class);
        MessageData messageMock3 = mock(MessageData.class);
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        Date date3 = new Date(date2.getTime() + 1000);
        when(messageMock1.getCreatedAt()).thenReturn(date1);
        when(messageMock2.getCreatedAt()).thenReturn(date2);
        when(messageMock3.getCreatedAt()).thenReturn(date3);
        List<MessageData> apiMessages = Arrays.asList(messageMock1, messageMock2,
                messageMock3);

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
        when(mPreferenceProviderMock.getPreference(Constants.LATEST_MESSAGE_DATE_KEY)).thenReturn(
                syncDate);

        MessageData messageMock1 = mock(MessageData.class);
        MessageData messageMock2 = mock(MessageData.class);
        MessageData messageMock3 = mock(MessageData.class);
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        Date date3 = new Date(date2.getTime() + 1000);
        when(messageMock1.getCreatedAt()).thenReturn(date1);
        when(messageMock2.getCreatedAt()).thenReturn(date2);
        when(messageMock3.getCreatedAt()).thenReturn(date3);
        List<MessageData> apiMessages = Arrays.asList(messageMock1, messageMock2,
                messageMock3);

        when(mGGMessagingAPIMock.getMessagesFromDate(syncDate)).thenReturn(
                Observable.just(apiMessages));

        //Act
        mMessagePoller.poll().subscribe();

        //Assert
        verify(mPolledMessagesProcessorMock, times(1)).process(any());
    }
}