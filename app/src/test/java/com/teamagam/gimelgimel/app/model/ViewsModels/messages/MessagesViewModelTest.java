package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MessagesViewModelTest {

    private MessagesViewModel mMessageViewModel;
    private InMemoryMessagesModel mMessagesModel;
    private InMemorySelectedMessageModel mSelectedMessageModel;
    private InMemoryMessagesReadStatusModel mMessagesReadStatusModel;

    private Message createMessage() {
        return mock(Message.class);
    }

    private DisplayMessage createDisplayMessage() {
        return new DisplayMessage(createMessage());
    }

    @Before
    public void setUp() throws Exception {
        mMessagesModel = new InMemoryMessagesModel();
        mSelectedMessageModel = spy(new InMemorySelectedMessageModel());
        mMessagesReadStatusModel = new InMemoryMessagesReadStatusModel();

        mMessageViewModel = new MessagesViewModel(mMessagesModel, mSelectedMessageModel,
                mMessagesReadStatusModel);
    }

    @Test
    public void selectMessageAfterObserverRegistration_shouldFireDataChanged() throws Exception {
        //Arrange
        DisplayMessage displayMessage = createDisplayMessage();

        DataChangedObserver dataChangedObserver = createDataChangedObserver();
        mMessageViewModel.addObserver(dataChangedObserver);

        //Act
        mMessageViewModel.select(displayMessage);

        //Assert
        verify(dataChangedObserver, atLeast(1)).onDataChanged();
    }


    private DataChangedObserver createDataChangedObserver() {
        return mock(DataChangedObserver.class);
    }

    @Test
    public void onMessageModelChangedAfterObserverRegistration_shouldFireDataChanged() throws Exception {
        //Arrange
        DataChangedObserver dataChangedObserver = createDataChangedObserver();
        mMessageViewModel.addObserver(dataChangedObserver);

        mMessagesModel.add(createMessage());

        //Assert
        verify(dataChangedObserver).onDataChanged();
    }

    @Test
    public void onSelectedModelChangedAfterObserverRegistration_shouldFireDataChanged() throws Exception {
        //Arrange
        DataChangedObserver dataChangedObserver = createDataChangedObserver();
        mMessageViewModel.addObserver(dataChangedObserver);
        mSelectedMessageModel.select(createMessage());

        //Assert
        verify(dataChangedObserver).onDataChanged();
    }

    @Test
    public void onMessagesReadStatusModelChangedAfterObserverRegistration_shouldFireDataChanged() throws Exception {
        //Arrange
        DataChangedObserver dataChangedObserver = createDataChangedObserver();
        mMessageViewModel.addObserver(dataChangedObserver);
        mMessagesReadStatusModel.markAsRead(createMessage());

        //Assert
        verify(dataChangedObserver).onDataChanged();
    }

    @Test
    public void selectMessageAfterAddingAndRemovingObserver_shouldNotFireDataChanged() throws Exception {
        //Arrange
        DataChangedObserver dataChangedObserver = createDataChangedObserver();
        mMessageViewModel.addObserver(dataChangedObserver);
        mMessageViewModel.removeObserver(dataChangedObserver);

        //Act
        mMessageViewModel.select(createDisplayMessage());

        //Assert
        verify(dataChangedObserver, never()).onDataChanged();
    }

    @Test
    public void getDisplayedMessagesRandomAccessor_shouldReturnRandomAccessorOfAppropriateSize() throws Exception {
        //Arrange
        mMessagesModel.add(createMessage());
        mMessagesModel.add(createMessage());
        mMessagesModel.add(createMessage());

        //Act
        MessagesViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
        assertThat(res.size(), equalTo(3));
    }

    @Test
    public void getDisplayedMessageRandomAccessor_shouldReturnMessagesInRightOrder() throws Exception {
        //Arrange
        Message m1 = createMessage();
        Message m2 = createMessage();
        Message m3 = createMessage();

        mMessagesModel.add(m1);
        mMessagesModel.add(m2);
        mMessagesModel.add(m3);

        //Act
        MessagesViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
        assertThat(res.get(0).getMessage(), sameInstance(m1));
        assertThat(res.get(1).getMessage(), sameInstance(m2));
        assertThat(res.get(2).getMessage(), sameInstance(m3));
    }

    @Test
    public void getDisplayedMessageRandomAccessorWithNoSelectedMessage_shouldReturnFirstMessageAsSelected() throws Exception {
        //Arrange
        mMessagesModel.add(createMessage());
        mMessagesModel.add(createMessage());

        //Act
        MessagesViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
        assertThat(res.get(1).isSelected(), is(false));
        assertThat(res.get(0).isSelected(), is(true));
    }


    @Test
    public void getDisplayedMessagesRandomAccessorWithNoSelectedMessage_shouldSetOnlyOneMessageAsSelected() throws Exception {
        //Arrange
        mMessagesModel.add(createMessage());
        mMessagesModel.add(createMessage());
        mMessagesModel.add(createMessage());

        //Act
        MessagesViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();
        res.get(0);
        res.get(1);
        res.get(2);

        //Assert
        verify(mSelectedMessageModel, times(1)).select(any(Message.class));
    }

    @Test
    public void getDisplayedMessageRandomAccessorSelectedMessage_shouldReturnAppropriateMessageAsSelected() throws Exception {
        //Arrange
        Message m1 = createMessage();
        Message m2 = createMessage();
        Message m3 = createMessage();

        mSelectedMessageModel.select(m2);

        mMessagesReadStatusModel.markAsRead(m1);
        mMessagesReadStatusModel.markAsRead(m3);

        mMessagesModel.add(m1);
        mMessagesModel.add(m2);
        mMessagesModel.add(m3);

        //Act
        MessagesViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
        assertThat(res.get(0).isSelected(), is(false));
        assertThat(res.get(1).isSelected(), is(true));
        assertThat(res.get(2).isSelected(), is(false));
    }

    @Test
    public void getDisplayMessageRandomAccessor_shouldReturnWithAppropriateReadStatus() throws Exception {
        //Arrange
        Message m1 = createMessage();
        Message m2 = createMessage();
        Message m3 = createMessage();

        mMessagesModel.add(m1);
        mMessagesModel.add(m2);
        mMessagesModel.add(m3);

        mMessagesReadStatusModel.markAsRead(m1);
        mMessagesReadStatusModel.markAsRead(m3);

        //Act
        MessagesViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
        assertThat(res.get(0).isRead(), is(true));
        assertThat(res.get(1).isRead(), is(false));
        assertThat(res.get(2).isRead(), is(true));
    }
}