package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.viewModel.MessagesMasterViewModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessagesViewModelTest {

    private MessagesMasterViewModel mMessageViewModel;
    private InMemoryMessagesModel mMessagesModel;
    private InMemorySelectedMessageModel mSelectedMessageModel;
    private InMemoryMessagesReadStatusModel mMessagesReadStatusModel;

    private MessageApp createMessage() {
        return mock(MessageApp.class);
    }

    private DisplayMessage createDisplayMessage() {
        return new DisplayMessage.DisplayMessageBuilder().setMessage(createMessage()).build();
    }

    private DataChangedObserver createDataChangedObserver() {
        return mock(DataChangedObserver.class);
    }

    @Before
    public void setUp() throws Exception {
        mMessagesModel = new InMemoryMessagesModel();
        mSelectedMessageModel = spy(new InMemorySelectedMessageModel());
        mMessagesReadStatusModel = spy(new InMemoryMessagesReadStatusModel());

//        mMessageViewModel = new MessagesMasterViewModel(mMessagesModel, mSelectedMessageModel,
//                mMessagesReadStatusModel);
    }

    @Test
    public void selectAlreadySelectedMessage_shouldNotReselect() throws Exception {
        //Arrange
        DisplayMessage displayMessage = createDisplayMessage();
        when(mSelectedMessageModel.getSelected()).thenReturn(displayMessage.getMessage());

        //Act
//        mMessageViewModel.select(displayMessage);

        //Assert
        verify(mSelectedMessageModel, never()).select(any(MessageApp.class));
    }

    @Test
    public void selectUnselectedMessage_shouldSelectMessage() throws Exception {
        //Arrange
        DisplayMessage displayMessage = createDisplayMessage();
        MessageApp otherMessage = createMessage();
        when(mSelectedMessageModel.getSelected()).thenReturn(otherMessage);

        //Act
//        mMessageViewModel.select(displayMessage);

        //Assert
        verify(mSelectedMessageModel, times(1)).select(displayMessage.getMessage());
    }

    @Test
    public void selectUnreadMessage_shouldMarkAsRead() throws Exception {
        //Arrange
        DisplayMessage dm = createDisplayMessage();
        when(mMessagesReadStatusModel.isRead(dm.getMessage())).thenReturn(false);

        //Act
//        mMessageViewModel.select(dm);

        //Assert
        verify(mMessagesReadStatusModel, times(1)).markAsRead(dm.getMessage());
    }

    @Test
    public void selectAlreadyReadMessage_shouldNotMarkAsRead() throws Exception {
        //Arrange
        DisplayMessage dm = createDisplayMessage();
        when(mMessagesReadStatusModel.isRead(dm.getMessage())).thenReturn(true);

        //Act
//        mMessageViewModel.select(dm);

        //Assert
        verify(mMessagesReadStatusModel, never()).markAsRead(dm.getMessage());
    }

    @Test
    public void onMessageModelChangedAfterObserverRegistration_shouldFireDataChanged() throws Exception {
        //Arrange
        DataChangedObserver dataChangedObserver = createDataChangedObserver();
//        mMessageViewModel.addObserver(dataChangedObserver);

        mMessagesModel.add(createMessage());

        //Assert
        verify(dataChangedObserver).onDataChanged();
    }

    @Test
    public void onSelectedModelChangedAfterObserverRegistration_shouldFireDataChanged() throws Exception {
        //Arrange
        DataChangedObserver dataChangedObserver = createDataChangedObserver();
//        mMessageViewModel.addObserver(dataChangedObserver);
        mSelectedMessageModel.select(createMessage());

        //Assert
        verify(dataChangedObserver).onDataChanged();
    }

    @Test
    public void onMessagesReadStatusModelChangedAfterObserverRegistration_shouldFireDataChanged() throws Exception {
        //Arrange
        DataChangedObserver dataChangedObserver = createDataChangedObserver();
//        mMessageViewModel.addObserver(dataChangedObserver);
        mMessagesReadStatusModel.markAsRead(createMessage());

        //Assert
        verify(dataChangedObserver).onDataChanged();
    }

    @Test
    public void selectMessageAfterAddingAndRemovingObserver_shouldNotFireDataChanged() throws Exception {
        //Arrange
        DataChangedObserver dataChangedObserver = createDataChangedObserver();
//        mMessageViewModel.addObserver(dataChangedObserver);
//        mMessageViewModel.removeObserver(dataChangedObserver);

        //Act
//        mMessageViewModel.select(createDisplayMessage());

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
//        MessagesMasterViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
//        assertThat(res.size(), equalTo(3));
    }

    @Test
    public void getDisplayedMessageRandomAccessor_shouldReturnMessagesInRightOrder() throws Exception {
        //Arrange
        MessageApp m1 = createMessage();
        MessageApp m2 = createMessage();
        MessageApp m3 = createMessage();

        mMessagesModel.add(m1);
        mMessagesModel.add(m2);
        mMessagesModel.add(m3);

        //Act
//        MessagesMasterViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
//        assertThat(res.get(0).getMessage(), sameInstance(m1));
//        assertThat(res.get(1).getMessage(), sameInstance(m2));
//        assertThat(res.get(2).getMessage(), sameInstance(m3));
    }

    @Test
    public void getDisplayedMessageRandomAccessorWithNoSelectedMessage_shouldReturnAllAsNotSelected() throws Exception {
        //Arrange
        mMessagesModel.add(createMessage());
        mMessagesModel.add(createMessage());

        //Act
//        MessagesMasterViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
//        assertThat(res.get(1).isSelected(), is(false));
//        assertThat(res.get(0).isSelected(), is(false));
    }

    @Test
    public void getDisplayedMessageRandomAccessorSelectedMessage_shouldReturnAppropriateMessageAsSelected() throws Exception {
        //Arrange
        MessageApp m1 = createMessage();
        MessageApp m2 = createMessage();
        MessageApp m3 = createMessage();

        mSelectedMessageModel.select(m2);

        mMessagesReadStatusModel.markAsRead(m1);
        mMessagesReadStatusModel.markAsRead(m3);

        mMessagesModel.add(m1);
        mMessagesModel.add(m2);
        mMessagesModel.add(m3);

        //Act
//        MessagesMasterViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
//        assertThat(res.get(0).isSelected(), is(false));
//        assertThat(res.get(1).isSelected(), is(true));
//        assertThat(res.get(2).isSelected(), is(false));
    }

    @Test
    public void getDisplayMessageRandomAccessor_shouldReturnWithAppropriateReadStatus() throws Exception {
        //Arrange
        MessageApp m1 = createMessage();
        MessageApp m2 = createMessage();
        MessageApp m3 = createMessage();

        mMessagesModel.add(m1);
        mMessagesModel.add(m2);
        mMessagesModel.add(m3);

        mMessagesReadStatusModel.markAsRead(m1);
        mMessagesReadStatusModel.markAsRead(m3);

        //Act
//        MessagesMasterViewModel.DisplayedMessagesRandomAccessor res = mMessageViewModel.getDisplayedMessagesRandomAccessor();

        //Assert
//        assertThat(res.get(0).isRead(), is(true));
//        assertThat(res.get(1).isRead(), is(false));
//        assertThat(res.get(2).isRead(), is(true));
    }
}