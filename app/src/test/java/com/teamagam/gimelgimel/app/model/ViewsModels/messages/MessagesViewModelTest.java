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
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MessagesViewModelTest {

    private MessagesViewModel mMessageViewModel;
    private InMemoryMessagesModel mMessagesModel;
    private InMemorySelectedMessageModel mSelectedMessageModel;
    private InMemoryMessagesReadStatusModel mMessagesReadStatusModel;
    private DataChangedObserver mDataChangedObserver;

    private Message createMessage() {
        return mock(Message.class);
    }

    @Before
    public void setUp() throws Exception {
        mMessagesModel = new InMemoryMessagesModel();
        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mMessagesReadStatusModel = new InMemoryMessagesReadStatusModel();

        mMessageViewModel = new MessagesViewModel(mMessagesModel, mSelectedMessageModel,
                mMessagesReadStatusModel);

        mDataChangedObserver = mock(DataChangedObserver.class);
        mMessageViewModel.setObserver(mDataChangedObserver);
    }


    @Test
    public void selectMessage_shouldFireDataChanged() throws Exception {
        //Arrange
        Message message = createMessage();
        DisplayMessage displayMessage = new DisplayMessage(message);

        //Act
        mMessageViewModel.select(displayMessage);

        //Assert
        verify(mDataChangedObserver, atLeast(1)).onDataChanged();
    }



    @Test
    public void onMessageModelChanged_shouldFireDataChanged() throws Exception {
        //Arrange
        mMessagesModel.add(createMessage());

        //Assert
        verify(mDataChangedObserver).onDataChanged();
    }

    @Test
    public void onSelectedModelChanged_shouldFireDataChanged() throws Exception {
        //Arrange
        mSelectedMessageModel.select(createMessage());

        //Assert
        verify(mDataChangedObserver).onDataChanged();
    }

    @Test
    public void onMessagesReadStatusModelChanged_shouldFireDataChanged() throws Exception {
        //Arrange
        mMessagesReadStatusModel.markAsRead(createMessage());

        //Assert
        verify(mDataChangedObserver).onDataChanged();

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
        assertThat(res.get(0).isSelected(), is(true));
        assertThat(res.get(1).isSelected(), is(false));
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