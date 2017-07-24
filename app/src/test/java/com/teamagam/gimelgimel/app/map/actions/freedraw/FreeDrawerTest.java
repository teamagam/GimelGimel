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
  public void drawingTest() {
    // Arrange
    List<PointGeometry> points = generatePoints(10, 0);
    List<MapDragEvent> mapDragEvents = generateDragEvents(points);

    // Act
    new FreeDrawer(this, Observable.fromIterable(mapDragEvents));

    // Assert
    assertEquals(1, mDisplayedMapEntities.size());
    assertEquals(points, extractPointsFromDisplayedEntity(0));
  }

  @Test
  public void drawingTestWithTwoDragStreams() {
    // Arrange
    List<PointGeometry> points1 = generatePoints(10, 0);
    List<PointGeometry> points2 = generatePoints(7, 23);
    List<MapDragEvent> mapDragEvents = generateDragEvents(points1);
    mapDragEvents.addAll(generateDragEvents(points2));

    // Act
    new FreeDrawer(this, Observable.fromIterable(mapDragEvents));

    // Assert
    assertEquals(2, mDisplayedMapEntities.size());
    assertEquals(points1, extractPointsFromDisplayedEntity(0));
    assertEquals(points2, extractPointsFromDisplayedEntity(1));
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

  private List<PointGeometry> extractPointsFromDisplayedEntity(int i) {
    return ((Polyline) mDisplayedMapEntities.get(i).getGeometry()).getPoints();
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