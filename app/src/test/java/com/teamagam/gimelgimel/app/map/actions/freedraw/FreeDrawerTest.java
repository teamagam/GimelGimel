package com.teamagam.gimelgimel.app.map.actions.freedraw;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapDragEvent;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.OnMapGestureListener;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FreeDrawerTest implements GGMapView {

  public static final String INITIAL_COLOR = "#test_initial_color";
  private List<GeoEntity> mDisplayedMapEntities;
  private MapEntityClickedListener mListener;
  private Observable<MapDragEvent> mDragEventObservable;

  @Before
  public void setUp() throws Exception {
    mDisplayedMapEntities = new ArrayList<>();
  }

  @Test
  public void draw() {
    // Arrange
    List<PointGeometry> points = generatePoints(10, 0);
    List<MapDragEvent> stream = generateDragEvents(points);
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    startFreeDrawer();

    // Assert
    assertDisplayedEntitiesByPoints(points);
  }

  @Test
  public void drawTwoEntities() {
    // Arrange
    List<PointGeometry> points1 = generatePoints(10, 0);
    List<PointGeometry> points2 = generatePoints(7, 23);
    List<MapDragEvent> streams = generateMapDragEventsOfTwoStreams(points1, points2);
    mDragEventObservable = Observable.fromIterable(streams);

    // Act
    startFreeDrawer();

    // Assert
    assertDisplayedEntitiesByPoints(points1, points2);
  }

  @Test
  public void undo() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.undo();

    // Assert
    assertEquals(0, mDisplayedMapEntities.size());
  }

  @Test
  public void undoWhenNothingDisplayedDoesNothing() {
    // Arrange
    mDragEventObservable = Observable.fromIterable(Collections.EMPTY_LIST);

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.undo();
  }

  @Test
  public void undoOneOfTwo() {
    // Arrange
    List<PointGeometry> points1 = generatePoints(10, 0);
    List<PointGeometry> points2 = generatePoints(7, 23);
    List<MapDragEvent> streams = generateMapDragEventsOfTwoStreams(points1, points2);
    mDragEventObservable = Observable.fromIterable(streams);

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.undo();

    // Assert
    assertDisplayedEntitiesByPoints(points1);
  }

  @Test
  public void initialColor() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    startFreeDrawer();

    // Assert
    assertEquals(INITIAL_COLOR,
        ((PolylineSymbol) mDisplayedMapEntities.get(0).getSymbol()).getBorderColor());
  }

  @Test
  public void setColor() {
    // Arrange
    List<MapDragEvent> stream1 = generateDragEvents(generatePoints(10, 0));
    List<MapDragEvent> stream2 = generateDragEvents(generatePoints(7, 23));
    SubjectRepository<MapDragEvent> subject = SubjectRepository.createReplayAll();
    String testColor = "test_other_color";
    mDragEventObservable = subject.getObservable();

    // Act
    FreeDrawer drawer = startFreeDrawer();
    publishStream(subject, stream1);
    drawer.setColor(testColor);
    publishStream(subject, stream2);

    // Assert
    assertEquals(INITIAL_COLOR,
        ((PolylineSymbol) mDisplayedMapEntities.get(0).getSymbol()).getBorderColor());
    assertEquals(testColor,
        ((PolylineSymbol) mDisplayedMapEntities.get(1).getSymbol()).getBorderColor());
  }

  @Test
  public void erase() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.switchMode();
    mListener.entityClicked(mDisplayedMapEntities.get(0).getId());

    // Assert
    assertEquals(0, mDisplayedMapEntities.size());
  }

  @Test
  public void notErasingOtherEntities() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.switchMode();
    mListener.entityClicked("no_such_id");

    // Assert
    assertEquals(1, mDisplayedMapEntities.size());
  }

  @Test
  public void eraserDisablesDrawing() {
    // Arrange
    List<PointGeometry> points = generatePoints(10, 0);
    List<MapDragEvent> stream1 = generateDragEvents(points);
    List<MapDragEvent> stream2 = generateDragEvents(generatePoints(7, 23));
    SubjectRepository<MapDragEvent> subject = SubjectRepository.createReplayAll();
    mDragEventObservable = subject.getObservable();

    // Act
    FreeDrawer drawer = startFreeDrawer();
    publishStream(subject, stream1);
    drawer.switchMode();
    publishStream(subject, stream2);

    // Assert
    assertDisplayedEntitiesByPoints(points);
  }

  @Test
  public void drawingEnabledWhenQuittingEraser() {
    // Arrange
    List<PointGeometry> points1 = generatePoints(10, 0);
    List<PointGeometry> points2 = generatePoints(7, 23);
    List<PointGeometry> points3 = generatePoints(12, 35);
    List<MapDragEvent> stream1 = generateDragEvents(points1);
    List<MapDragEvent> stream2 = generateDragEvents(points2);
    List<MapDragEvent> stream3 = generateDragEvents(points3);
    SubjectRepository<MapDragEvent> subject = SubjectRepository.createReplayAll();

    // Act
    mDragEventObservable = subject.getObservable();
    FreeDrawer drawer = startFreeDrawer();
    publishStream(subject, stream1);
    drawer.switchMode(); // to eraser
    publishStream(subject, stream2);
    drawer.switchMode(); // to drawing
    publishStream(subject, stream3);

    // Assert
    assertDisplayedEntitiesByPoints(points1, points3);
  }

  @Test
  public void whenEraserDisabled_notErasing() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.switchMode();
    drawer.switchMode();

    // Assert
    assertNull(mListener);
  }

  @Test
  public void tolerance() {
    // Arrange
    List<MapDragEvent> events =
        generateMapDragEventsOfTwoStreams(generatePoints(10, 1), generatePoints(10, 10.05));
    mDragEventObservable = Observable.fromIterable(events);

    // Act
    startFreeDrawer(0.1);

    // Assert
    assertEquals(1, mDisplayedMapEntities.size());
  }

  @Test
  public void isInEraserMode() {
    // Arrange
    mDragEventObservable = Observable.empty();

    // Act
    FreeDrawer drawer = startFreeDrawer();

    // Assert
    assertEquals(false, drawer.isInEraserMode());
    drawer.switchMode();
    assertEquals(true, drawer.isInEraserMode());
  }

  @Test
  public void undoErase() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.switchMode();
    mListener.entityClicked(mDisplayedMapEntities.get(0).getId());

    // Assert
    assertEquals(0, mDisplayedMapEntities.size());
    drawer.undo();
    assertEquals(1, mDisplayedMapEntities.size());
  }

  private void publishStream(SubjectRepository<MapDragEvent> subject, List<MapDragEvent> stream1) {
    for (MapDragEvent event : stream1) {
      subject.add(event);
    }
  }

  private List<PointGeometry> generatePoints(int count, double offset) {
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

  private FreeDrawer startFreeDrawer() {
    return startFreeDrawer(0);
  }

  private FreeDrawer startFreeDrawer(double tolerance) {
    return new FreeDrawer(this, INITIAL_COLOR, tolerance);
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
    if (notification.getGeoEntity() == null) {
      String action = notification.getAction() == 1 ? "ADD" : "REMOVE";
      throw new RuntimeException("FreeDrawer tried to " + action + " null object");
    }

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
    mListener = mapEntityClickedListener;
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
    return mDragEventObservable;
  }

  @Override
  public void setAllowPanning(boolean allow) {

  }
}