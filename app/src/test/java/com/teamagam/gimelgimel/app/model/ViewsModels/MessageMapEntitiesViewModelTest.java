package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageMapEntitiesViewModelTest {

    private InMemorySelectedMessageModel mSelectedMessageModel;
    private MessageMapEntitiesViewModel mMessageMapEntitiesViewModel;

    @Before
    public void setUp() throws Exception {
        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mMessageMapEntitiesViewModel = new MessageMapEntitiesViewModel(mSelectedMessageModel);
    }

    @Test
    public void addMessageLatLong_shouldAddMessageAndReturnEntity() throws Exception {
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        MessageLatLong messageLatLong = mock(MessageLatLong.class);
        when(messageLatLong.getType()).thenReturn(Message.LAT_LONG);
        when(messageLatLong.getContent()).thenReturn(pg);

        //Act
        Entity entity = mMessageMapEntitiesViewModel.addMessage(messageLatLong);

        //Assert
        assertThat(entity, instanceOf(Point.class));
    }

    @Test
    public void addMessageImage_shouldAddMessageAndReturnEntity() throws Exception {
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        MessageImage messageImage = mock(MessageImage.class);
        ImageMetadata ImageMetadata = mock(ImageMetadata.class);
        when(ImageMetadata.getLocation()).thenReturn(pg);
        when(messageImage.getType()).thenReturn(Message.IMAGE);
        when(messageImage.getContent()).thenReturn(ImageMetadata);

        //Act
        Entity entity = mMessageMapEntitiesViewModel.addMessage(messageImage);

        //Assert
        assertThat(entity, instanceOf(Point.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMessage_wrongMessageType_shouldThrowException() throws Exception {
        //Arrange
        Message msg = mock(Message.class);
        when(msg.getType()).thenReturn(Message.TEXT);

        //Act
        mMessageMapEntitiesViewModel.addMessage(msg);
    }

    @Test
    public void onEntityClick_shouldSelectMessage(){
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        Message messageLatLong = mock(MessageLatLong.class);
        when(messageLatLong.getType()).thenReturn(Message.LAT_LONG);
        when(messageLatLong.getContent()).thenReturn(pg);
        Entity entity = mMessageMapEntitiesViewModel.addMessage(messageLatLong);

        //Act
        entity.clicked();
        Message selected = mSelectedMessageModel.getSelected();

        //Assert
        assertThat(selected, equalTo(messageLatLong));
    }

}