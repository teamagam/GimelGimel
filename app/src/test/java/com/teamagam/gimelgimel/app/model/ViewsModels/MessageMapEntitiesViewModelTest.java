package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.content.Context;
import android.content.res.Resources;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.message.model.MessageGeo;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;
import com.teamagam.gimelgimel.app.model.entities.ImageMetadata;
import com.teamagam.gimelgimel.app.model.entities.messages.InMemory.InMemorySelectedMessageModel;
import com.teamagam.gimelgimel.app.map.model.symbols.IMessageSymbolizer;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.map.model.symbols.EntityMessageSymbolizer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class MessageMapEntitiesViewModelTest {

    private InMemorySelectedMessageModel mSelectedMessageModel;
    private MessageMapEntitiesViewModel mMessageMapEntitiesViewModel;

    @Before
    public void setUp() throws Exception {
        Context context = mock(Context.class);
        Resources res = mock(Resources.class);
        when(context.getResources()).thenReturn(res);
        when(res.getStringArray(anyInt())).thenReturn(new String[] {"type1", "type2"});
        IMessageSymbolizer symblizer = new EntityMessageSymbolizer(context);

        mSelectedMessageModel = new InMemorySelectedMessageModel();
        mMessageMapEntitiesViewModel = new MessageMapEntitiesViewModel(mSelectedMessageModel, symblizer);
    }



    @Test
    public void addMessageLatLong_shouldAddMessageAndReturnEntity() throws Exception {
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        GeoContent geoContent = mock(GeoContent.class);
        MessageGeo messageLatLong = mock(MessageGeo.class);
        when(messageLatLong.getType()).thenReturn(Message.GEO);
        when(messageLatLong.getContent()).thenReturn(geoContent);
        when(geoContent.getPointGeometry()).thenReturn(pg);

        //Act
        Entity entity = mMessageMapEntitiesViewModel.addReceivedMessage(messageLatLong);

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
        Entity entity = mMessageMapEntitiesViewModel.addReceivedMessage(messageImage);

        //Assert
        assertThat(entity, instanceOf(Point.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMessage_wrongMessageType_shouldThrowException() throws Exception {
        //Arrange
        Message msg = mock(Message.class);
        when(msg.getType()).thenReturn(Message.TEXT);

        //Act
        mMessageMapEntitiesViewModel.addReceivedMessage(msg);
    }

    @Test
    public void onEntityClick_shouldSelectMessage(){
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        GeoContent geoContent = mock(GeoContent.class);
        Message messageLatLong = mock(MessageGeo.class);
        when(messageLatLong.getType()).thenReturn(Message.GEO);
        when(messageLatLong.getContent()).thenReturn(geoContent);
        when(geoContent.getPointGeometry()).thenReturn(pg);
        Entity entity = mMessageMapEntitiesViewModel.addReceivedMessage(messageLatLong);

        //Act
        entity.clicked();
        Message selected = mSelectedMessageModel.getSelected();

        //Assert
        assertThat(selected, equalTo(messageLatLong));
    }

    @Test
    public void addSentMessageGeo_shouldAddMessageAndReturnEntity() throws Exception {
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        GeoContent geoContent = mock(GeoContent.class);
        MessageGeo messageLatLong = mock(MessageGeo.class);
        when(messageLatLong.getType()).thenReturn(Message.GEO);
        when(messageLatLong.getContent()).thenReturn(geoContent);
        when(geoContent.getPointGeometry()).thenReturn(pg);

        //Act
        Entity entity = mMessageMapEntitiesViewModel.addSentMessage(messageLatLong);

        //Assert
        assertThat(entity, instanceOf(Point.class));
    }

    @Test
    public void onSentEntityClick_shouldDoNothing(){
        //Arrange
        PointGeometry pg = mock(PointGeometry.class);
        GeoContent geoContent = mock(GeoContent.class);
        Message messageLatLong = mock(MessageGeo.class);
        when(messageLatLong.getType()).thenReturn(Message.GEO);
        when(messageLatLong.getContent()).thenReturn(geoContent);
        when(geoContent.getPointGeometry()).thenReturn(pg);
        Entity entity = mMessageMapEntitiesViewModel.addSentMessage(messageLatLong);

        //Act
        entity.clicked();
        Message selected = mSelectedMessageModel.getSelected();

        //Assert
        assertThat(selected, not(equalTo(messageLatLong)));
    }

}