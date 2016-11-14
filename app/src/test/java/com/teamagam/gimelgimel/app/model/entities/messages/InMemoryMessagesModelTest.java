package com.teamagam.gimelgimel.app.model.entities.messages;

import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemoryMessagesModel;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InMemoryMessagesModelTest {

    private InMemoryMessagesModel mMessageModel;
    private MessageApp mMessage;
    private DataChangedObserver mObserver;

    private DataChangedObserver createListener() {
        return mock(DataChangedObserver.class);
    }


    private MessageApp createMockMessage() {
        return mock(MessageApp.class);
    }

    @Before
    public void setUp() throws Exception {
        mMessageModel = new InMemoryMessagesModel();
        mMessage = createMockMessage();
        mObserver = createListener();
    }

    @Test
    public void afterAddMessage_sizeShouldBeOne() throws Exception {
        //Act
        mMessageModel.add(mMessage);

        //Assert
        assertThat(mMessageModel.size(), equalTo(1));
    }

    @Test
    public void afterAddMessage_associatedIndexShouldReturnMessage() throws Exception {
        //Act
        int idx = mMessageModel.add(mMessage);

        //Assert
        assertThat(mMessageModel.get(idx), sameInstance(mMessage));
    }

    @Test
    public void afterAddingTwoMessages_eachCanBeAccessedThroughAssociatedIdx() throws Exception {
        //Arrange
        MessageApp message2 = createMockMessage();

        //Act
        int idx1 = mMessageModel.add(mMessage);
        int idx2 = mMessageModel.add(message2);

        //Assert
        assertThat(mMessageModel.get(idx1), sameInstance(mMessage));
        assertThat(mMessageModel.get(idx2), sameInstance(message2));
    }

    @Test(expected = MessagesModel.AlreadyExistsException.class)
    public void addingSameMessageTwice_shouldThrow() throws Exception {
        //Act
        mMessageModel.add(mMessage);
        mMessageModel.add(mMessage);
    }

    @Test
    public void removedByAssociatedId_shouldDecreaseSize() throws Exception {
        //Arrange
        int idx = mMessageModel.add(mMessage);
        int beforeSize = mMessageModel.size();

        //Act
        mMessageModel.remove(idx);

        //Assert
        assertThat(mMessageModel.size(), equalTo(beforeSize - 1));
    }

    @Test
    public void remove_shouldOffsetIdxFromItsIdx() throws Exception {
        //Arrange
        MessageApp m2 = createMockMessage();
        MessageApp m3 = createMockMessage();
        int idx1 = mMessageModel.add(mMessage);
        int idx2 = mMessageModel.add(m2);
        int idx3 = mMessageModel.add(m3);

        //Act
        mMessageModel.remove(idx2);

        //Assert
        assertThat(mMessageModel.get(idx1), sameInstance(mMessage));
        assertThat(mMessageModel.get(idx3 - 1), sameInstance(m3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void removeFromTheEndThenGetOnThatIdx_shouldThrow() throws Exception {
        //Arrange
        mMessageModel.add(mMessage);
        mMessageModel.add(createMockMessage());
        int idx3 = mMessageModel.add(createMockMessage());

        //Act
        mMessageModel.remove(idx3);
        mMessageModel.get(idx3);
    }

    @Test
    public void afterAdd_onDataChangedIsFired() throws Exception {
        //Arrange
//        mMessageModel.addObserver(mObserver);

        //Act
        mMessageModel.add(createMockMessage());

        //Assert
        verify(mObserver, times(1)).onDataChanged();
    }

    @Test
    public void afterRemove_OnDataChangedIsFired() throws Exception {
        //Arrange
        int idx = mMessageModel.add(createMockMessage());
//        mMessageModel.addObserver(mObserver);

        //Act
        mMessageModel.remove(idx);

        //Assert
        verify(mObserver, times(1)).onDataChanged();
    }
}