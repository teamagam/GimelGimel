//package com.teamagam.gimelgimel.app.model.ViewsModels.messages;
//
//import com.teamagam.gimelgimel.app.common.DataChangedObserver;
//import com.teamagam.gimelgimel.app.message.viewModel.MessageDetailViewModel;
//import com.teamagam.gimelgimel.app.message.model.MessageApp;
//import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
//import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
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
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class MessageDetailViewModelTest {
//
//    private static final String COMPATIBLE_MESSAGE_TYPE = MessageApp.IMAGE;
//    private static final String INCOMPATIBLE_MESSAGE_TYPE = MessageApp.TEXT;
//
//    private MessageApp createCompatibleTypeMessage() {
//        return createMockMessageWithType(COMPATIBLE_MESSAGE_TYPE);
//    }
//
//    private MessageApp createIncompatibleMessage() {
//        return createMockMessageWithType(INCOMPATIBLE_MESSAGE_TYPE);
//    }
//
//    private MessageApp createMockMessageWithType(@MessageApp.MessageType String messageType) {
//        MessageApp m = mock(MessageApp.class);
//        when(m.getType()).thenReturn(messageType);
//        return m;
//    }
//
//    private DataChangedObserver createAndAttachObserver() {
//        DataChangedObserver observer = mock(DataChangedObserver.class);
//        mMessageDetailViewModel.addObserver(observer);
//        return observer;
//    }
//
//    private SelectedMessageModel mSelectedMessageModel;
//    private MessageDetailViewModel mMessageDetailViewModel;
//
//    @Before
//    public void setUp() throws Exception {
//        mSelectedMessageModel = spy(new InMemorySelectedMessageModel());
//        mMessageDetailViewModel = new MessageDetailViewModel(mSelectedMessageModel) {
//            @Override
//            protected String getExpectedMessageType() {
//                return COMPATIBLE_MESSAGE_TYPE;
//            }
//        };
//    }
//
//
//    @Test
//    public void getDateOfCompatibleType_shouldReturnSelectedDate() throws Exception {
//        //Arrange
//        Date d = new Date();
//        MessageApp m = createCompatibleTypeMessage();
//        when(m.getCreatedAt()).thenReturn(d);
//        mSelectedMessageModel.select(m);
//
//        //Act
//        Date res = mMessageDetailViewModel.getDate();
//
//        //Assert
//        assertThat(res, equalTo(d));
//    }
//
//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getDateOfIncompatibleType_shouldThrow() throws Exception {
//        //Arrange
//        MessageApp m = createIncompatibleMessage();
//        mSelectedMessageModel.select(m);
//
//        //Act
//        mMessageDetailViewModel.getDate();
//    }
//
//    @Test
//    public void getSenderIdOfCompatibleType_shouldReturnSelectedSenderId() throws Exception {
//        //Arrange
//        MessageApp m = createCompatibleTypeMessage();
//        String senderId = "senderid";
//        when(m.getSenderId()).thenReturn(senderId);
//        mSelectedMessageModel.select(m);
//
//        //Act
//        String res = mMessageDetailViewModel.getSenderId();
//
//        //Assert
//        assertThat(res, equalTo(senderId));
//    }
//
//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getSenderIdOfIncompatibleType_shouldThrow() throws Exception {
//        //Arrange
//        MessageApp m = createIncompatibleMessage();
//        mSelectedMessageModel.select(m);
//
//        //Act
//        mMessageDetailViewModel.getSenderId();
//    }
//
//    @Test
//    public void getTypeOfCompatibleType_shouldReturnSelectedType() throws Exception {
//        //Arrange
//        MessageApp m = createCompatibleTypeMessage();
//        mSelectedMessageModel.select(m);
//
//        //Act
//        String res = mMessageDetailViewModel.getType();
//
//        //Assert
//        assertThat(res, equalTo(COMPATIBLE_MESSAGE_TYPE));
//    }
//
//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getTypeOfIncompatibleType_shouldThrow() throws Exception {
//        //Arrange
//        MessageApp m = createIncompatibleMessage();
//        mSelectedMessageModel.select(m);
//
//        //Act
//        mMessageDetailViewModel.getType();
//    }
//
//    @Test
//    public void selectedMessageChangedToCompatibleType_shouldNotifyObserver() throws Exception {
//        //Arrange
//        MessageApp m = createCompatibleTypeMessage();
//        mSelectedMessageModel.select(m);
//        DataChangedObserver observer = createAndAttachObserver();
//        MessageApp newCompatibleMessage = createCompatibleTypeMessage();
//
//        //Act
//        mSelectedMessageModel.select(newCompatibleMessage);
//
//        //Assert
//        verify(observer, times(1)).onDataChanged();
//    }
//
//    @Test
//    public void selectedMessageChangedToIncompatibleType_shouldNotNotify() throws Exception {
//        //Arrange
//        MessageApp m = createCompatibleTypeMessage();
//        mSelectedMessageModel.select(m);
//        DataChangedObserver observer = createAndAttachObserver();
//        MessageApp newIncompatibleMessage = createIncompatibleMessage();
//
//        //Act
//        mSelectedMessageModel.select(newIncompatibleMessage);
//
//        //Assert
//        verify(observer, never()).onDataChanged();
//    }
//}