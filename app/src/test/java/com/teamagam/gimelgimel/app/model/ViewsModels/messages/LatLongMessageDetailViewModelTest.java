package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageLatLong;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LatLongMessageDetailViewModelTest {

    private InMemorySelectedMessageModel mSelectedMessageModel;
    private LatLongMessageDetailViewModel mLatLongMessageDetailViewModel;

    @Before
    public void setUp() throws Exception {
        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mLatLongMessageDetailViewModel = new LatLongMessageDetailViewModel(mSelectedMessageModel);
    }

    @Test
    public void getPointGeometry_shouldReturnSelectedPointGeometry() throws Exception {
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        MessageLatLong messageLatLong = mock(MessageLatLong.class);
        when(messageLatLong.getType()).thenReturn(Message.LAT_LONG);
        when(messageLatLong.getContent()).thenReturn(pg);
        mSelectedMessageModel.select(messageLatLong);

        //Act
        PointGeometry res = mLatLongMessageDetailViewModel.getPointGeometry();

        //Assert
        assertThat(res, equalTo(pg));
    }

    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
    public void getPointGeometryWithIncompatibleMessage_shouldThrow() throws Exception {
        //Arrange
        Message m = mock(Message.class);
        when(m.getType()).thenReturn(Message.USER_LOCATION);
        mSelectedMessageModel.select(m);

        //Act
        mLatLongMessageDetailViewModel.getPointGeometry();
    }
}