package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.message.model.MessageGeoModel;
import com.teamagam.gimelgimel.app.message.viewModel.GeoMessageDetailViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeoMessageDetailViewModelTest {

    private InMemorySelectedMessageModel mSelectedMessageModel;
    private GeoMessageDetailViewModel mLatLongMessageDetailViewModel;

    @Before
    public void setUp() throws Exception {
        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mLatLongMessageDetailViewModel = new GeoMessageDetailViewModel();
    }

    @Test
    public void getPointGeometry_shouldReturnSelectedPointGeometry() throws Exception {
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        GeoContent geoContent = mock(GeoContent.class);
        MessageGeoModel messageLatLong = mock(MessageGeoModel.class);
        when(messageLatLong.getType()).thenReturn(Message.GEO);
        when(messageLatLong.getContent()).thenReturn(geoContent);
        when(geoContent.getPointGeometry()).thenReturn(pg);
        mSelectedMessageModel.select(messageLatLong);

        //Act
        PointGeometry res = mLatLongMessageDetailViewModel.getPointGeometry();

        //Assert
        assertThat(res, equalTo(pg));
    }

//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getPointGeometryWithIncompatibleMessage_shouldThrow() throws Exception {
//        //Arrange
//        Message m = mock(Message.class);
//        when(m.getType()).thenReturn(Message.USER_LOCATION);
//        mSelectedMessageModel.select(m);
//
//        //Act
//        mLatLongMessageDetailViewModel.getPointGeometry();
//    }
}