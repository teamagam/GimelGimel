package com.teamagam.gimelgimel.app.map.actions.freedraw;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapDragEvent;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FreeDrawerTest implements GGMapView {

  private List<GeoEntity> mDisplayedMapEntities;

  @Before
  public void setUp() throws Exception {
    mDisplayedMapEntities = new ArrayList<>();
  }

  @Test
  public void draw() {
    // Arrange
    List<PointGeometry> points = generatePoints(10, 0);

    // Act
    startFreeDrawer(generateDragEvents(points));

    // Assert
    assertDisplayedEntitiesByPoints(points);
  }

  @Test
  public void drawTwoDragStreams() {
    // Arrange
    List<PointGeometry> points1 = generatePoints(10, 0);
    List<PointGeometry> points2 = generatePoints(7, 23);
    List<MapDragEvent> mapDragEvents = generateMapDragEventsOfTwoStreams(points1, points2);

    // Act
    startFreeDrawer(mapDragEvents);

    // Assert
    assertDisplayedEntitiesByPoints(points1, points2);
  }

  @Test
  public void undoWhenNothingDisplayed() {
    // Act
    FreeDrawer drawer = startFreeDrawer(Collections.EMPTY_LIST);
    drawer.undo();
  }

  @Test
  public void undo() {
    // Arrange
    List<PointGeometry> points1 = generatePoints(10, 0);
    List<PointGeometry> points2 = generatePoints(7, 23);
    List<MapDragEvent> mapDragEvents = generateMapDragEventsOfTwoStreams(points1, points2);

    // Act
    FreeDrawer drawer = startFreeDrawer(mapDragEvents);
    drawer.undo();

    // Assert
    assertDisplayedEntitiesByPoints(points1);
  }

  private List<PointGeometry> generatePoints(int count, int offset) {
    List<PointGeometry> points = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      points.add(new PointGeometry(i + offset, i + offset));
    }
    return points;
  }

  private List<MapDragEvent> generateDragEvents(List<PointGeometry> points) {
    List<MapDragEvent> events = new ArrayList<>();
    for (int i = 0; i < points.size() - 1; i++) {
      events.add(new MapDragEvent(points.get(i), points.get(i + 1)));
    }
    return events;
  }

  private FreeDrawer startFreeDrawer(List<MapDragEvent> mapDragEvents) {
    return new FreeDrawer(this, Observable.fromIterable(mapDragEvents));
  }

  @SafeVarargs
  private final void assertDisplayedEntitiesByPoints(List<PointGeometry>... points) {
    assertEquals(points.length, mDisplayedMapEntities.size());
    for (int i = 0; i < points.length; i++) {
      assertEquals(points[i], extractPointsFromDisplayedEntity(i));
    }
  }

  private List<PointGeometry> extractPointsFromDisplayedEntity(int index) {
    return ((Polyline) mDisplayedMapEntities.get(index).getGeometry()).getPoints();
  }

  private List<MapDragEvent> generateMapDragEventsOfTwoStreams(List<PointGeometry> pts1,
      List<PointGeometry> pts2) {
    List<MapDragEvent> events = generateDragEvents(pts1);
    events.addAll(generateDragEvents(pts2));
    return events;
  }

  @Override
  public void updateMapEntity(GeoEntityNotification notification) {
    if (notification.getAction() == GeoEntityNotification.ADD) {
      mDisplayedMapEntities.add(notification.getGeoEntity());
    } else if (notification.getAction() == GeoEntityNotification.REMOVE) {
      mDisplayedMapEntities.remove(notification.getGeoEntity());
    }
  }

  @Override
  public void lookAt(Geometry geometry) {

  }

  @Override
  public void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener) {

  }

  @Override
  public void showVectorLayer(VectorLayerPresentation vectorLayerPresentation) {

  }

  @Override
  public void hideVectorLayer(String vectorLayerId) {

  }

  @Override
  public void setOnReadyListener(OnReadyListener onReadyListener) {

  }

  @Override
  public void setOnMapGestureListener(OnMapGestureListener onMapGestureListener) {

  }

  @Override
  public void saveState() {

  }

  @Override
  public void restoreState() {

  }

  @Override
  public void centerOverCurrentLocationWithAzimuth() {

  }

  @Override
  public void setIntermediateRaster(IntermediateRaster intermediateRaster) {

  }

  @Override
  public void removeIntermediateRaster() {

  }

  @Override
  public PointGeometry getMapCenter() {
    return null;
  }

  @Override
  public Observable<MapDragEvent> getMapDragEventObservable() {
    return null;
  }

  @Override
  public void setAllowPanning(boolean allow) {

  }
}