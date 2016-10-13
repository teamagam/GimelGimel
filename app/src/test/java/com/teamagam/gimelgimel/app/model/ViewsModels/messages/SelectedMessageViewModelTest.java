//package com.teamagam.gimelgimel.app.model.ViewsModels.messages;
//
//import com.teamagam.gimelgimel.app.common.DataChangedObserver;
//import com.teamagam.gimelgimel.app.message.viewModel.MessageDetailViewModel;
//import com.teamagam.gimelgimel.app.message.viewModel.SelectedMessageViewModel;
//import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
//import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.Date;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.junit.Assert.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class SelectedMessageViewModelTest {
//
//    private Message createMockMessageAndSetAsSelected() {
//        Message m = mock(Message.class);
//        mSelectedMessageModel.select(m);
//        return m;
//    }
//
//    private DataChangedObserver createDataChangedObserverMock() {
//        return mock(DataChangedObserver.class);
//    }
//
//    private SelectedMessageViewModel<V> mSelectedMessageViewModel;
//    private InMemorySelectedMessageModel mSelectedMessageModel;
//
//    @Before
//    public void setUp() throws Exception {
//        mSelectedMessageModel = new InMemorySelectedMessageModel();
//        mSelectedMessageViewModel = new SelectedMessageViewModel<V>(mSelectedMessageModel) {
//            @Override
//            protected boolean shouldNotifyOnSelectedMessageModelChange() {
//                return false;
//            }
//        };
//    }
//
////    @Test(expected = MessageDetailViewModel.NoSelectedMessageException.class)
////    public void getTypeWithNoSelectedMessage_shouldThrow() throws Exception {
////        mSelectedMessageViewModel.getType();
////    }
////
////    @Test(expected = MessageDetailViewModel.NoSelectedMessageException.class)
////    public void getSenderIdWithNoSelectedMessage_shouldThrow() throws Exception {
////        mSelectedMessageViewModel.getSenderId();
////    }
////
////    @Test(expected = MessageDetailViewModel.NoSelectedMessageException.class)
////    public void getMessageDateWithNoSelectedMessage_shouldThrow() throws Exception {
////        mSelectedMessageViewModel.getDate();
////    }
//
//    @Test
//    public void getTypeWithSelectedMessage_shouldReturnSelectedMessageType() throws Exception {
//        //Arrange
//        String type = "type";
//        Message m = createMockMessageAndSetAsSelected();
//        when(m.getType()).thenReturn(type);
//
//        //Act
//        String result = mSelectedMessageViewModel.getType();
//
//        //Assert
//        assertThat(result, equalTo(type));
//    }
//
//    @Test
//    public void getSenderId_shouldReturnSelectedMessageSenderId() throws Exception {
//        //Arrange
//        String senderId = "exampleSenderId";
//        Message m = createMockMessageAndSetAsSelected();
//        when(m.getSenderId()).thenReturn(senderId);
//
//        //Act
//        String res = mSelectedMessageViewModel.getSenderId();
//
//        //Assert
//        assertThat(res, equalTo(senderId));
//    }
//
//    @Test
//    public void getDate_shouldReturnSelectedMessageDate() throws Exception {
//        //Arrange
//        Date d = new Date();
//        Message m = createMockMessageAndSetAsSelected();
//        when(m.getCreatedAt()).thenReturn(d);
//
//        //Act
//        Date res = mSelectedMessageViewModel.getDate();
//
//        //Assert
//        assertThat(res, equalTo(d));
//    }
//
//    @Test
//    public void onSelectedMessageAndShouldNotifyViewModel_shouldNotifyObserver() throws Exception {
//        //Arrange
//        SelectedMessageViewModel<V> shouldNotifySelectedMessageViewModel = new SelectedMessageViewModel<V>(
//                mSelectedMessageModel) {
//            @Override
//            protected boolean shouldNotifyOnSelectedMessageModelChange() {
//                return true;
//            }
//        };
//        DataChangedObserver observer = createDataChangedObserverMock();
//        shouldNotifySelectedMessageViewModel.addObserver(observer);
//
//        //Act
//        createMockMessageAndSetAsSelected();
//
//        //Verify
//        verify(observer, times(1)).onDataChanged();
//    }
//
//    @Test
//    public void onSelectedMessageAndShouldNotNotifyViewModel_shouldNotNotify() throws Exception {
//        //Arrange
//        SelectedMessageViewModel<V> shouldNotNotifySelectedMessageViewModel = new SelectedMessageViewModel<V>(
//                mSelectedMessageModel) {
//            @Override
//            protected boolean shouldNotifyOnSelectedMessageModelChange() {
//                return false;
//            }
//        };
//        DataChangedObserver observer = createDataChangedObserverMock();
//        shouldNotNotifySelectedMessageViewModel.addObserver(observer);
//
//        //Act
//        createMockMessageAndSetAsSelected();
//
//        //Verify
//        verify(observer, never()).onDataChanged();
//    }
//}