package com.teamagam.gimelgimel.app.model.ViewsModels.messages;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageImage;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageMessageDetailViewModelTest {

    private void createAndSelectMessageImage(ImageMetadata imageMetadata) {
        MessageImage message = mock(MessageImage.class);
        when(message.getType()).thenReturn(Message.IMAGE);
        when(message.getContent()).thenReturn(imageMetadata);
        mSelectedMessageModel.select(message);
    }

    private void createAndSelectIncompatibleMessage() {
        Message m = mock(Message.class);
        when(m.getType()).thenReturn(Message.TEXT);
        mSelectedMessageModel.select(m);
    }

    private ImageMetadata createMockImageMetadata() {
        return mock(ImageMetadata.class);
    }

    private SelectedMessageModel mSelectedMessageModel;
    private ImageMessageDetailViewModel mMessageDetailViewModel;

    @Before
    public void setUp() throws Exception {
        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mMessageDetailViewModel = new ImageMessageDetailViewModel(mSelectedMessageModel);
    }

    @Test
    public void getImageUrl_shouldReturnSelectedMessageUrl() throws Exception {
        //Arrange
        String url = "urlish";
        ImageMetadata imageMetadata = createMockImageMetadata();
        when(imageMetadata.getURL()).thenReturn(url);
        createAndSelectMessageImage(imageMetadata);

        //Act
        String res = mMessageDetailViewModel.getImageUrl();

        //Assert
        assertThat(res, equalTo(url));
    }


    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
    public void getImageUrlWithIncompatibleMessageType_shouldThrow() throws Exception {
        //Arrange
        createAndSelectIncompatibleMessage();

        //Act
        mMessageDetailViewModel.getImageUrl();
    }

    @Test
    public void getImageDate_shouldReturnSelectedMessageDate() throws Exception {
        //Arrange
        Date d = new Date();
        ImageMetadata imageMetadata = createMockImageMetadata();
        when(imageMetadata.getTime()).thenReturn(d.getTime());
        createAndSelectMessageImage(imageMetadata);

        //Act
        Date res = mMessageDetailViewModel.getImageDate();

        //Assert
        assertThat(res, equalTo(d));
    }

    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
    public void getImageDateWhenIncompatibleMessage_shouldThrow() throws Exception {
        //Arrange
        createAndSelectIncompatibleMessage();

        //Act
        mMessageDetailViewModel.getImageDate();
    }

    @Test
    public void hasLocation_shouldReturnSelectedImageHasLocation() throws Exception {
        //Arrange
        boolean hasLocation = true;
        ImageMetadata imageMetadata = createMockImageMetadata();
        when(imageMetadata.hasLocation()).thenReturn(hasLocation);
        createAndSelectMessageImage(imageMetadata);

        //Act
        boolean res = mMessageDetailViewModel.hasLocation();

        //Assert
        assertThat(res, equalTo(hasLocation));
    }

    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
    public void hasLocationWithIncompatibleMessage_shouldThrow() throws Exception {
        //Arrange
        createAndSelectIncompatibleMessage();

        //Act
        mMessageDetailViewModel.hasLocation();
    }

    @Test
    public void getPointGeometry_shouldReturnSelectedMessagePointGeometry() throws Exception {
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        ImageMetadata imageMetadata = createMockImageMetadata();
        when(imageMetadata.getLocation()).thenReturn(pg);
        createAndSelectMessageImage(imageMetadata);

        //Act
        PointGeometry res = mMessageDetailViewModel.getPointGeometry();

        assertThat(res, equalTo(pg));
    }

    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
    public void getPointGeometryWithIncompatibleMessage_shouldThrow() throws Exception {
        //Arrange
        createAndSelectIncompatibleMessage();

        //Act
        mMessageDetailViewModel.getPointGeometry();
    }

    @Test
    public void getImageSourceWithCompatibleMessage_shouldReturnSelectedMessageSource() throws Exception {
        //Arrange
        @ImageMetadata.SourceType String sourceType = ImageMetadata.USER;
        ImageMetadata imageMetadata = createMockImageMetadata();
        when(imageMetadata.getSource()).thenReturn(sourceType);
        createAndSelectMessageImage(imageMetadata);

        //Act
        String res = mMessageDetailViewModel.getImageSource();

        //Assert
        assertThat(res, equalTo(sourceType));
    }

    @Test(expected = MessageDetailViewModel.IncompatibleMessageType.class)
    public void getImageSourceWithIncompatibleMessage_shouldThrow() throws Exception {
        //Arrange
        createAndSelectIncompatibleMessage();

        //Act
        mMessageDetailViewModel.getImageSource();
    }
}