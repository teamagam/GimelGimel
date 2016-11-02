package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageGeoApp;
import com.teamagam.gimelgimel.app.message.viewModel.GeoMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
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
        PointGeometryApp pg = mock(PointGeometryApp.class);
        GeoContentApp geoContent = mock(GeoContentApp.class);
        MessageGeoApp messageLatLong = mock(MessageGeoApp.class);
        when(messageLatLong.getType()).thenReturn(MessageApp.GEO);
        when(messageLatLong.getContent()).thenReturn(geoContent);
//        when(geoContent.getPointGeometry()).thenReturn(pg);
        mSelectedMessageModel.select(messageLatLong);

        //Act
        PointGeometryApp res = mLatLongMessageDetailViewModel.getPointGeometry();

        //Assert
        assertThat(res, equalTo(pg));
    }

//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getPointGeometryWithIncompatibleMessage_shouldThrow() throws Exception {
//        //Arrange
//        MessageApp m = mock(MessageApp.class);
//        when(m.getType()).thenReturn(MessageApp.USER_LOCATION);
//        mSelectedMessageModel.select(m);
//
//        //Act
//        mLatLongMessageDetailViewModel.getPointGeometry();
//    }
}