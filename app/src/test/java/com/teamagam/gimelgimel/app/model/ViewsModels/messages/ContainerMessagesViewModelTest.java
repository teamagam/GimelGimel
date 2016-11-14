package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.viewModel.ContainerMessagesViewModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContainerMessagesViewModelTest {

    private MessageApp createMessageInsideModel() {
        MessageApp message = mock(MessageApp.class);
        mMessagesModel.add(message);
        return message;
    }

    private DataChangedObserver createAndAttachObserver() {
        DataChangedObserver observer = mock(DataChangedObserver.class);
//        mContainerMessagesViewModel.addObserver(observer);
        return observer;
    }

    private ContainerMessagesViewModel mContainerMessagesViewModel;
    private InMemorySelectedMessageModel mSelectedMessageModel;
    private InMemoryMessagesReadStatusModel mMessagesReadStatusModel;
    private InMemoryMessagesModel mMessagesModel;

    @Before
    public void setUp() throws Exception {
        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mMessagesReadStatusModel = new InMemoryMessagesReadStatusModel();
        mMessagesModel = new InMemoryMessagesModel();
//        mContainerMessagesViewModel = new ContainerMessagesViewModel(mSelectedMessageModel,
//                mMessagesReadStatusModel, mMessagesModel);
    }

    @Test
    public void isMessageSelectedWithSelectedMessage_shouldReturnTrue() throws Exception {
        //Arrange
        MessageApp m = createMessageInsideModel();
        mSelectedMessageModel.select(m);

        //Act
        boolean res = mContainerMessagesViewModel.isAnyMessageSelected();

        //Assert
        assertThat(res, is(true));
    }

    @Test
    public void isMessageSelectedWithNoSelectedMessage_shouldReturnFalse() throws Exception {
        //Act
        boolean res = mContainerMessagesViewModel.isAnyMessageSelected();

        //Assert
        assertThat(res, is(false));
    }

    @Test
    public void getUnreadMessageWithNoMessagesAtAll_shouldReturnZero() throws Exception {
        //Act
//        int res = mContainerMessagesViewModel.getUnreadMessageCount();

        //Assert
//        assertThat(res, equalTo(0));
    }

    @Test
    public void getUnreadMessageCountWithOneMessageUnreadAndOneMessageRead_shouldReturnOne() throws Exception {
        //Arrange
        mMessagesReadStatusModel.markAsRead(createMessageInsideModel());
        createMessageInsideModel();

        //Act
//        int res = mContainerMessagesViewModel.getUnreadMessageCount();

        //Assert
//        assertThat(res, equalTo(1));
    }

//    @Test(expected = SelectedMessageViewModel.NoSelectedMessageException.class)
//    public void getMessageContentWithNoSelectedMessage_shouldThrow() throws Exception {
//        //Act
////        mContainerMessagesViewModel.getMessageContent();
//    }

    @Test
    public void getMessageContentWithSelectedMessage_shouldReturnSelectedContent() throws Exception {
        //Arrange
        Object fictiveContent = new Object();
        MessageApp m = createMessageInsideModel();
        when(m.getContent()).thenReturn(fictiveContent);
        mSelectedMessageModel.select(m);

        //Act
//        Object res = mContainerMessagesViewModel.getMessageContent();

        //Assert
//        assertThat(res, sameInstance(fictiveContent));
    }

    @Test
    public void onSelectedMessageModelChange_shouldNotifyObserver() throws Exception {
        //Arrange
        MessageApp m = createMessageInsideModel();
        DataChangedObserver observer = createAndAttachObserver();

        //Act
        mSelectedMessageModel.select(m);

        //Arrange
        verify(observer, times(1)).onDataChanged();
    }

    @Test
    public void onMessagesModelChange_shouldNotifyObserver() throws Exception {
        //Arrange
        DataChangedObserver observer = createAndAttachObserver();

        //Act
        createMessageInsideModel();

        //Assert
        verify(observer, times(1)).onDataChanged();
    }

    @Test
    public void onReadMessageModelChanged_shouldNotifyObserver() throws Exception {
        //Arrange
        MessageApp m = createMessageInsideModel();
        DataChangedObserver observer = createAndAttachObserver();

        //Act
        mMessagesReadStatusModel.markAsRead(m);

        //Assert
        verify(observer, times(1)).onDataChanged();
    }
}