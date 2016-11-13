/*
package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.message.viewModel.TextMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TextMessageDetailViewModelTest {

    private void insertSelectedMessageWithText(String text) {
        MessageTextApp m = mock(MessageTextApp.class);
        when(m.getType()).thenReturn(MessageApp.TEXT);
        when(m.getContent()).thenReturn(text);
        mSelectedMessageModel.select(m);
    }

    private SelectedMessageModel mSelectedMessageModel;
    private TextMessageDetailViewModel mTextMessageDetailViewModel;

    @Before
    public void setUp() throws Exception {
        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mTextMessageDetailViewModel = new TextMessageDetailViewModel();
    }

    @Test
    public void getText_shouldReturnSelectedMessageText() throws Exception {
        //Arrange
        String text = "myText";
        insertSelectedMessageWithText(text);

        //Act
        String res = mTextMessageDetailViewModel.getText();

        //Assert
        assertThat(res, equalTo(text));
    }

//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getTextWithIncompatibleMessage_shouldThrow() throws Exception {
//        //Arrange
//        MessageApp m = mock(MessageApp.class);
//        when(m.getmLocationType()).thenReturn(MessageApp.IMAGE);
//        mSelectedMessageModel.select(m);
//
//        //Act
//        mTextMessageDetailViewModel.getText();
//    }
}*/
