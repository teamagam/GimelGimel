package com.teamagam.gimelgimel.app.map;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.domain.dynamicLayers.DynamicLayerPresentation;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DynamicLayersInteractorDisplayerTest {

  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String DESCRIPTION = "";

  private GGMapView mMapSpy;
  private DynamicLayersMapDisplayer mDisplayer;

  private GeoEntity mEntity1;
  private DynamicLayer mDynamicLayer;
  private GeoEntity mEntity2;
  private GeoEntity mEntity3;
  private DynamicLayer mUpdatedDynamicLayer;

  public static DynamicLayer createTestLayer(GeoEntity... entities) {
    return createTestLayer(Lists.newArrayList(entities));
  }

  public static DynamicLayer createTestLayer(List<GeoEntity> entities) {
    return new DynamicLayer(ID, NAME, "", 0, toDynamicEntities(entities));
  }

  private static List<DynamicEntity> toDynamicEntities(List<GeoEntity> entities) {
    return Lists.transform(entities, e -> new DynamicEntity(e, DESCRIPTION));
  }

  @Before
  public void setUp() throws Exception {
    mMapSpy = spy(GGMapView.class);
    mDisplayer = new DynamicLayersMapDisplayer(mMapSpy);

    mEntity1 = mock(GeoEntity.class);
    when(mEntity1.getId()).thenReturn("entity_id_1");
    mEntity2 = mock(GeoEntity.class);
    when(mEntity2.getId()).thenReturn("entity_id_2");
    mEntity3 = mock(GeoEntity.class);
    when(mEntity3.getId()).thenReturn("entity_id_3");
  }

  @Test
  public void displayNewShownLayer() throws Exception {
    // Arrange
    mDynamicLayer = createTestLayer(mEntity1);

    // Act
    mDisplayer.display(createDynamicLayerPresentation(mDynamicLayer, true));

    // Assert
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createAdd(mEntity1));
  }

  @Test
  public void whenNewHiddenLayer_shouldNotUpdateMap() throws Exception {
    //Arrange
    mDynamicLayer = mock(DynamicLayer.class);

    //Act
    mDisplayer.display(createDynamicLayerPresentation(mDynamicLayer, false));

    //Assert
    verify(mMapSpy, never()).updateMapEntity(any());
  }

  @Test
  public void whenHidingDisplayingLayer_shouldRemoveEntities() throws Exception {
    //Arrange
    mDynamicLayer = createTestLayer(mEntity1);
    mDisplayer.display(createDynamicLayerPresentation(mDynamicLayer, true));

    //Act
    mDisplayer.display(createDynamicLayerPresentation(mDynamicLayer, false));

    //Assert
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createAdd(mEntity1));
    verify(mMapSpy, times(1)).updateMapEntity(GeoEntityNotification.createRemove(mEntity1));
  }

  @Test
  public void displayNewLayerManyEntities() throws Exception {
    // Arrange
    mDynamicLayer = createTestLayer(mEntity1, mEntity2, mEntity3);

    // Act
    mDisplayer.display(createDynamicLayerPresentation(mDynamicLayer, true));

    // Assert
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createAdd(mEntity1));
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createAdd(mEntity2));
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createAdd(mEntity3));
  }

  @Test
  public void removeEntity() throws Exception {
    // Arrange
    mDynamicLayer = createTestLayer(mEntity1);
    mUpdatedDynamicLayer = createTestLayer(new GeoEntity[] {});
    mDisplayer.display(createDynamicLayerPresentation(mDynamicLayer, true));

    // Act
    mDisplayer.display(createDynamicLayerPresentation(mUpdatedDynamicLayer, true));

    // Assert
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createAdd(mEntity1));
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createRemove(mEntity1));
  }

  @Test
  public void addEntity() throws Exception {
    // Arrange
    mDynamicLayer = createTestLayer(mEntity1);
    mUpdatedDynamicLayer = createTestLayer(mEntity1, mEntity2);
    mDisplayer.display(createDynamicLayerPresentation(mDynamicLayer, true));

    // Act
    mDisplayer.display(createDynamicLayerPresentation(mUpdatedDynamicLayer, true));

    // Expect:
    // createAdd(mEntity1) ->
    // createRemove(mEntity1) ->
    // createAdd(mEntity1) ->
    // createAdd(mEntity2) .
    verify(mMapSpy, times(2)).updateMapEntity(GeoEntityNotification.createAdd(mEntity1));
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createRemove(mEntity1));
    verify(mMapSpy).updateMapEntity(GeoEntityNotification.createAdd(mEntity2));
  }

  private DynamicLayerPresentation createDynamicLayerPresentation(DynamicLayer dynamicLayer,
      boolean isShown) {
    return new DynamicLayerPresentation(dynamicLayer, isShown);
  }
}