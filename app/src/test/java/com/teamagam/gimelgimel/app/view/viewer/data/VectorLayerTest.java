package com.teamagam.gimelgimel.app.view.viewer.data;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;

import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class VectorLayerTest {

    private static final String LAYER_ID = "layerId";
    private static final String ENTITY_ID = "entityId";

    private VectorLayer mVectorLayer;
    private Entity mEntityMock;
    private VectorLayer.LayerChangedListener mLayerChangedListenerMock;


    @Before
    public void setUp() throws Exception {
        mVectorLayer = new VectorLayer(LAYER_ID);

        mEntityMock = mock(Entity.class);
        when(mEntityMock.getId()).thenReturn(ENTITY_ID);

        mLayerChangedListenerMock = mock(VectorLayer.LayerChangedListener.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEntity_nullArgument_shouldThrow() throws Exception {
        //Arrange
        Entity e = null;

        //Act
        mVectorLayer.addEntity(e);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEntity_noIdEntity_shouldThrow() throws Exception {
        //Arrange
        Entity e = mock(Entity.class);

        //Act
        mVectorLayer.addEntity(e);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEntity_ExistingId_shouldThrow() throws Exception {
        //Arrange
        Entity entity = mEntityMock;
        Entity entityWithSameId = mock(Entity.class);
        when(entityWithSameId.getId()).thenReturn(ENTITY_ID);

        //Act
        mVectorLayer.addEntity(entity);
        mVectorLayer.addEntity(entityWithSameId);
    }

    @Test
    public void testAddEntity_validEntity_shouldNotifyLayerChanged() throws Exception {
        //Arrange
        Entity entity = mEntityMock;

        mVectorLayer.setOnLayerChangedListener(mLayerChangedListenerMock);

        //Act
        mVectorLayer.addEntity(entity);

        //Assert
        ArgumentCaptor<LayerChangedEventArgs> eventArgs = ArgumentCaptor.forClass(
                LayerChangedEventArgs.class);

        verify(mLayerChangedListenerMock).layerChanged(eventArgs.capture());

        assertThat(eventArgs.getValue().entity, sameInstance(entity));
        assertThat(eventArgs.getValue().layerId, equalTo(LAYER_ID));
        assertThat(eventArgs.getValue().eventType,
                equalTo(LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_ADD));
    }

    @Test
    public void testAddEntity_getEntity_validEntity_shouldBeContained() throws Exception {
        //Arrange
        Entity entity = mEntityMock;

        //Act
        mVectorLayer.addEntity(entity);

        //Assert
        assertThat(entity, equalTo(mVectorLayer.getEntity(ENTITY_ID)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEntity_nullInput_shouldThrow() throws Exception {
        //Arrange
        String entityId = null;

        //Act
        mVectorLayer.removeEntity(entityId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEntity_emptyInput_shouldThrow() throws Exception {
        //Arrange
        String entityId = "";

        //Act
        mVectorLayer.removeEntity(entityId);
    }

    @Test
    public void testRemoveEntity_validInput_shouldNotifyLayerChanged() throws Exception {
        //Arrange
        String entityId = mEntityMock.getId();
        mVectorLayer.setOnLayerChangedListener(mLayerChangedListenerMock);
        mVectorLayer.addEntity(mEntityMock);

        ArgumentCaptor<LayerChangedEventArgs> eventArgs = ArgumentCaptor.forClass(
                LayerChangedEventArgs.class);

        //Act
        mVectorLayer.removeEntity(entityId);

        //Assert
        verify(mLayerChangedListenerMock, times(2)).layerChanged(eventArgs.capture());

        LayerChangedEventArgs layerChangedEventArgs = eventArgs.getValue();

        assertThat(layerChangedEventArgs.entity, sameInstance(mEntityMock));
        assertThat(layerChangedEventArgs.layerId, equalTo(LAYER_ID));
        assertThat(layerChangedEventArgs.eventType,
                equalTo(LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_REMOVE));
    }

    @Test
    public void testRemoveEntity_getEntity_validInput_shouldNotContain() throws Exception {
        //Arrange
        String entityId = mEntityMock.getId();
        mVectorLayer.setOnLayerChangedListener(mLayerChangedListenerMock);
        mVectorLayer.addEntity(mEntityMock);

        //Act
        mVectorLayer.removeEntity(entityId);

        //Assert
        assertThat(mVectorLayer.getEntity(ENTITY_ID), nullValue());
        assertThat(mVectorLayer.getEntities(), empty());
    }


    @Test
    public void testGetEntities_emptyVectorLayer_expectEmptyCollection() throws Exception {
        //Act
        Collection<Entity> entities = mVectorLayer.getEntities();

        //Assert
        assertThat(entities, empty());
    }

    @Test
    public void testGetEntities_withEntities_shouldReturnAll() throws Exception {
        //Arrange
        Entity e1 = mock(Entity.class);
        Entity e2 = mock(Entity.class);
        Entity e3 = mock(Entity.class);
        when(e1.getId()).thenReturn("e1");
        when(e2.getId()).thenReturn("e2");
        when(e3.getId()).thenReturn("e3");
        mVectorLayer.addEntity(e1);
        mVectorLayer.addEntity(e2);
        mVectorLayer.addEntity(e3);

        //Act
        Collection<Entity> entities = mVectorLayer.getEntities();

        //Assert
        assertThat(entities, hasItems(e1, e2, e3));
        assertThat(entities, IsCollectionWithSize.hasSize(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntity_nullInput_shouldThrow() throws Exception {
        //Arrange
        String entityId = null;

        //Act
        mVectorLayer.getEntity(entityId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntity_emptyInput_shouldThrow() throws Exception {
        //Arrange
        String entityId = "";

        //Act
        mVectorLayer.getEntity(entityId);
    }

    @Test
    public void testGetEntity_validInput_multipleValues_shouldReturnMatching() throws Exception {
        //Arrange
        Entity e1 = mock(Entity.class);
        Entity e2 = mock(Entity.class);
        Entity e3 = mock(Entity.class);
        when(e1.getId()).thenReturn("e1");
        when(e2.getId()).thenReturn("e2");
        when(e3.getId()).thenReturn("e3");
        mVectorLayer.addEntity(e1);
        mVectorLayer.addEntity(e2);
        mVectorLayer.addEntity(e3);

        //Act
        Entity actual = mVectorLayer.getEntity("e1");

        //Assert
        assertThat(actual, sameInstance(e1));
    }
}