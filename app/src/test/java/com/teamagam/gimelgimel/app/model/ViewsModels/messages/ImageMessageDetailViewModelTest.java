package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.model.contents.ImageMetadataApp;
import com.teamagam.gimelgimel.app.message.viewModel.ImageMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.model.MessageImageApp;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageMessageDetailViewModelTest {

    private void createAndSelectMessageImage(ImageMetadataApp imageMetadata) {
        MessageImageApp message = mock(MessageImageApp.class);
        when(message.getType()).thenReturn(MessageApp.IMAGE);
        when(message.getContent()).thenReturn(imageMetadata);
        mSelectedMessageModel.select(message);
    }

    private void createAndSelectIncompatibleMessage() {
        MessageApp m = mock(MessageApp.class);
        when(m.getType()).thenReturn(MessageApp.TEXT);
        mSelectedMessageModel.select(m);
    }

    private ImageMetadataApp createMockImageMetadata() {
        return mock(ImageMetadataApp.class);
    }

    private SelectedMessageModel mSelectedMessageModel;
    private ImageMessageDetailViewModel mMessageDetailViewModel;

    @Before
    public void setUp() throws Exception {
        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mMessageDetailViewModel = new ImageMessageDetailViewModel();
    }

    @Test
    public void getImageUrl_shouldReturnSelectedMessageUrl() throws Exception {
        //Arrange
        String url = "urlish";
        ImageMetadataApp imageMetadata = createMockImageMetadata();
        when(imageMetadata.getURL()).thenReturn(url);
        createAndSelectMessageImage(imageMetadata);

        //Act
        String res = mMessageDetailViewModel.getImageUri().toString();

        //Assert
        assertThat(res, equalTo(url));
    }


//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getImageUrlWithIncompatibleMessageType_shouldThrow() throws Exception {
//        //Arrange
//        createAndSelectIncompatibleMessage();
//
//        //Act
//        mMessageDetailViewModel.getImageUrl();
//    }

    @Test
    public void getImageDate_shouldReturnSelectedMessageDate() throws Exception {
        //Arrange
        Date d = new Date();
        ImageMetadataApp imageMetadata = createMockImageMetadata();
        when(imageMetadata.getTime()).thenReturn(d.getTime());
        createAndSelectMessageImage(imageMetadata);

        //Act
        Date res = mMessageDetailViewModel.getImageDate();

        //Assert
        assertThat(res, equalTo(d));
    }

//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getImageDateWhenIncompatibleMessage_shouldThrow() throws Exception {
//        //Arrange
//        createAndSelectIncompatibleMessage();
//
//        //Act
//        mMessageDetailViewModel.getImageDate();
//    }

    @Test
    public void hasLocation_shouldReturnSelectedImageHasLocation() throws Exception {
        //Arrange
        boolean hasLocation = true;
        ImageMetadataApp imageMetadata = createMockImageMetadata();
        when(imageMetadata.hasLocation()).thenReturn(hasLocation);
        createAndSelectMessageImage(imageMetadata);

        //Act
        boolean res = mMessageDetailViewModel.hasLocation();

        //Assert
        assertThat(res, equalTo(hasLocation));
    }
//
//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void hasLocationWithIncompatibleMessage_shouldThrow() throws Exception {
//        //Arrange
//        createAndSelectIncompatibleMessage();
//
//        //Act
//        mMessageDetailViewModel.isHasLocation();
//    }

    @Test
    public void getPointGeometry_shouldReturnSelectedMessagePointGeometry() throws Exception {
        //Arrange
        PointGeometryApp pg = mock(PointGeometryApp.class);
        ImageMetadataApp imageMetadata = createMockImageMetadata();
        when(imageMetadata.getLocation()).thenReturn(pg);
        createAndSelectMessageImage(imageMetadata);

        //Act
        PointGeometryApp res = mMessageDetailViewModel.getPointGeometry();

        assertThat(res, equalTo(pg));
    }

//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getPointGeometryWithIncompatibleMessage_shouldThrow() throws Exception {
//        //Arrange
//        createAndSelectIncompatibleMessage();
//
//        //Act
//        mMessageDetailViewModel.getPointGeometry();
//    }

    @Test
    public void getImageSourceWithCompatibleMessage_shouldReturnSelectedMessageSource() throws Exception {
        //Arrange
        @ImageMetadataApp.SourceType String sourceType = ImageMetadataApp.USER;
        ImageMetadataApp imageMetadata = createMockImageMetadata();
        when(imageMetadata.getSource()).thenReturn(sourceType);
        createAndSelectMessageImage(imageMetadata);

        //Act
        String res = mMessageDetailViewModel.getImageSource();

        //Assert
        assertThat(res, equalTo(sourceType));
    }

//    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
//    public void getImageSourceWithIncompatibleMessage_shouldThrow() throws Exception {
//        //Arrange
//        createAndSelectIncompatibleMessage();
//
//        //Act
//        mMessageDetailViewModel.getImageSource();
//    }
}