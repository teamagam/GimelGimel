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
import io.reactivex.observers.TestObserver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

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

  @Test
  public void ignoreDragEventsWithNoMove() {
    // Arrange
    PointGeometry p = new PointGeometry(0, 0);
    List<MapDragEvent> stream = Collections.singletonList(new MapDragEvent(p, p));
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    startFreeDrawer();

    // Assert
    assertEquals(0, mDisplayedMapEntities.size());
  }

  @Test
  public void testRealData() {
    // Arrange
    List<MapDragEvent> stream = getRealDragEventsData();
    mDragEventObservable = Observable.fromIterable(stream);

    // Act
    startFreeDrawer(0.00001);

    // Assert
    assertEquals(2, mDisplayedMapEntities.size());
  }

  @Test
  public void disable() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    SubjectRepository<MapDragEvent> subject = SubjectRepository.createReplayAll();
    mDragEventObservable = subject.getObservable();

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.disable();
    publishStream(subject, stream);

    // Assert
    assertEquals(0, mDisplayedMapEntities.size());
  }

  @Test
  public void enable() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    SubjectRepository<MapDragEvent> subject = SubjectRepository.createReplayAll();
    mDragEventObservable = subject.getObservable();

    // Act
    FreeDrawer drawer = startFreeDrawer();
    drawer.disable();
    drawer.enable();
    publishStream(subject, stream);

    // Assert
    assertEquals(1, mDisplayedMapEntities.size());
  }

  @Test
  public void getEntities() {
    // Arrange
    List<PointGeometry> points1 = generatePoints(10, 0);
    List<PointGeometry> points2 = generatePoints(7, 23);
    List<MapDragEvent> streams = generateMapDragEventsOfTwoStreams(points1, points2);
    mDragEventObservable = Observable.fromIterable(streams);

    // Act
    FreeDrawer drawer = startFreeDrawer();

    // Assert
    assertThat(drawer.getEntities(), containsInAnyOrder(mDisplayedMapEntities.toArray()));
  }

  @Test
  public void signalOnStartDrawingObservable() {
    // Arrange
    List<MapDragEvent> stream = generateDragEvents(generatePoints(10, 0));
    SubjectRepository<MapDragEvent> subject = SubjectRepository.createReplayAll();
    mDragEventObservable = subject.getObservable();

    // Act
    FreeDrawer drawer = startFreeDrawer();
    TestObserver<Object> test = drawer.getSignalOnStartDrawingObservable().test();

    // Assert
    test.assertNoValues();
    publishStream(subject, stream);
    test.assertValues(SIGNAL);
    publishStream(subject, stream);
    test.assertValues(SIGNAL, SIGNAL);
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
    FreeDrawer drawer = new FreeDrawer(this, INITIAL_COLOR, tolerance);
    drawer.start();
    return drawer;
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
  public void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener) {
    mListener = mapEntityClickedListener;
  }

  private List<MapDragEvent> getRealDragEventsData() {
    // data from 2 real streams of drag-events
    List<MapDragEvent> events = new ArrayList<>();
    events.add(new MapDragEvent(new PointGeometry(34.72866088687784, 32.04634121305788),
        new PointGeometry(34.72866088687784, 32.04634121305788)));
    events.add(new MapDragEvent(new PointGeometry(34.72866088687784, 32.04634121305788),
        new PointGeometry(34.72866088687784, 32.04634121305788)));
    events.add(new MapDragEvent(new PointGeometry(34.72866088687784, 32.04634121305788),
        new PointGeometry(34.730255295023454, 32.05014126517723)));
    events.add(new MapDragEvent(new PointGeometry(34.730255295023454, 32.05014126517723),
        new PointGeometry(34.73139953190206, 32.05204059269087)));
    events.add(new MapDragEvent(new PointGeometry(34.73139953190206, 32.05204059269087),
        new PointGeometry(34.73451978577962, 32.055905783890466)));
    events.add(new MapDragEvent(new PointGeometry(34.73451978577962, 32.055905783890466),
        new PointGeometry(34.751172159574736, 32.07452584602788)));
    events.add(new MapDragEvent(new PointGeometry(34.751172159574736, 32.07452584602788),
        new PointGeometry(34.75719597801548, 32.08038924213946)));
    events.add(new MapDragEvent(new PointGeometry(34.75719597801548, 32.08038924213946),
        new PointGeometry(34.7686410455236, 32.09040267046243)));
    events.add(new MapDragEvent(new PointGeometry(34.7686410455236, 32.09040267046243),
        new PointGeometry(34.780110635277296, 32.098065975399514)));
    events.add(new MapDragEvent(new PointGeometry(34.780110635277296, 32.098065975399514),
        new PointGeometry(34.78512612511689, 32.10109572754128)));
    events.add(new MapDragEvent(new PointGeometry(34.78512612511689, 32.10109572754128),
        new PointGeometry(34.79579519317594, 32.10634644124516)));
    events.add(new MapDragEvent(new PointGeometry(34.79579519317594, 32.10634644124516),
        new PointGeometry(34.80075045051545, 32.108546830896465)));
    events.add(new MapDragEvent(new PointGeometry(34.80075045051545, 32.108546830896465),
        new PointGeometry(34.805377175638455, 32.110412879820636)));
    events.add(new MapDragEvent(new PointGeometry(34.805377175638455, 32.110412879820636),
        new PointGeometry(34.81044772365683, 32.11187279327446)));
    events.add(new MapDragEvent(new PointGeometry(34.81044772365683, 32.11187279327446),
        new PointGeometry(34.814287016949386, 32.11315407483632)));
    events.add(new MapDragEvent(new PointGeometry(34.814287016949386, 32.11315407483632),
        new PointGeometry(34.81854618463578, 32.114357720676935)));
    events.add(new MapDragEvent(new PointGeometry(34.81854618463578, 32.114357720676935),
        new PointGeometry(34.822233520754885, 32.11523740251343)));
    events.add(new MapDragEvent(new PointGeometry(34.822233520754885, 32.11523740251343),
        new PointGeometry(34.82612734098174, 32.116141913077435)));
    events.add(new MapDragEvent(new PointGeometry(34.82612734098174, 32.116141913077435),
        new PointGeometry(34.82957016651263, 32.116694332160506)));
    events.add(new MapDragEvent(new PointGeometry(34.82957016651263, 32.116694332160506),
        new PointGeometry(34.83267577929208, 32.11718070511743)));
    events.add(new MapDragEvent(new PointGeometry(34.83267577929208, 32.11718070511743),
        new PointGeometry(34.836097673790135, 32.11772841152591)));
    events.add(new MapDragEvent(new PointGeometry(34.836097673790135, 32.11772841152591),
        new PointGeometry(34.83891535205689, 32.1181573307325)));
    events.add(new MapDragEvent(new PointGeometry(34.83891535205689, 32.1181573307325),
        new PointGeometry(34.84084928386746, 32.118403861176944)));
    events.add(new MapDragEvent(new PointGeometry(34.84084928386746, 32.118403861176944),
        new PointGeometry(34.84318910771693, 32.11877104596436)));
    events.add(new MapDragEvent(new PointGeometry(34.84318910771693, 32.11877104596436),
        new PointGeometry(34.84467330919343, 32.11881103629755)));
    events.add(new MapDragEvent(new PointGeometry(34.84467330919343, 32.11881103629755),
        new PointGeometry(34.84595148343245, 32.11894202161363)));
    events.add(new MapDragEvent(new PointGeometry(34.84595148343245, 32.11894202161363),
        new PointGeometry(34.84663160387624, 32.11894202161363)));
    events.add(new MapDragEvent(new PointGeometry(34.84663160387624, 32.11894202161363),
        new PointGeometry(34.847189049342774, 32.11894202161363)));
    events.add(new MapDragEvent(new PointGeometry(34.847189049342774, 32.11894202161363),
        new PointGeometry(34.847579524666614, 32.11894202161363)));
    events.add(new MapDragEvent(new PointGeometry(34.847579524666614, 32.11894202161363),
        new PointGeometry(34.847876384086724, 32.11894202161363)));
    events.add(new MapDragEvent(new PointGeometry(34.847876384086724, 32.11894202161363),
        new PointGeometry(34.84803966739267, 32.11894202161363)));
    events.add(new MapDragEvent(new PointGeometry(34.880059632062455, 32.069018780319794),
        new PointGeometry(34.880059632062455, 32.069018780319794)));
    events.add(new MapDragEvent(new PointGeometry(34.880059632062455, 32.069018780319794),
        new PointGeometry(34.880059632062455, 32.069018780319794)));
    events.add(new MapDragEvent(new PointGeometry(34.880059632062455, 32.069018780319794),
        new PointGeometry(34.879315464783154, 32.06690135444369)));
    events.add(new MapDragEvent(new PointGeometry(34.879315464783154, 32.06690135444369),
        new PointGeometry(34.878287719195946, 32.06476088320682)));
    events.add(new MapDragEvent(new PointGeometry(34.878287719195946, 32.06476088320682),
        new PointGeometry(34.87710145025341, 32.06246548849693)));
    events.add(new MapDragEvent(new PointGeometry(34.87710145025341, 32.06246548849693),
        new PointGeometry(34.875047319064905, 32.05901424730759)));
    events.add(new MapDragEvent(new PointGeometry(34.875047319064905, 32.05901424730759),
        new PointGeometry(34.87332277195698, 32.05647694614228)));
    events.add(new MapDragEvent(new PointGeometry(34.87332277195698, 32.05647694614228),
        new PointGeometry(34.87091376945023, 32.05338909039422)));
    events.add(new MapDragEvent(new PointGeometry(34.87091376945023, 32.05338909039422),
        new PointGeometry(34.86753171828881, 32.04983410551749)));
    events.add(new MapDragEvent(new PointGeometry(34.86753171828881, 32.04983410551749),
        new PointGeometry(34.863032714965705, 32.04599922386321)));
    events.add(new MapDragEvent(new PointGeometry(34.863032714965705, 32.04599922386321),
        new PointGeometry(34.858118023455326, 32.04206912928699)));
    events.add(new MapDragEvent(new PointGeometry(34.858118023455326, 32.04206912928699),
        new PointGeometry(34.853631961247956, 32.038623747656885)));
    events.add(new MapDragEvent(new PointGeometry(34.853631961247956, 32.038623747656885),
        new PointGeometry(34.84822570921249, 32.03477562441571)));
    events.add(new MapDragEvent(new PointGeometry(34.84822570921249, 32.03477562441571),
        new PointGeometry(34.84381371311169, 32.03172701089687)));
    events.add(new MapDragEvent(new PointGeometry(34.84381371311169, 32.03172701089687),
        new PointGeometry(34.83912154929261, 32.02872039739319)));
    events.add(new MapDragEvent(new PointGeometry(34.83912154929261, 32.02872039739319),
        new PointGeometry(34.8337120247911, 32.024953542245896)));
    events.add(new MapDragEvent(new PointGeometry(34.8337120247911, 32.024953542245896),
        new PointGeometry(34.8285012282278, 32.0221242049828)));
    events.add(new MapDragEvent(new PointGeometry(34.8285012282278, 32.0221242049828),
        new PointGeometry(34.824029456497165, 32.019494967471914)));
    events.add(new MapDragEvent(new PointGeometry(34.824029456497165, 32.019494967471914),
        new PointGeometry(34.814232150018626, 32.01469291408076)));
    events.add(new MapDragEvent(new PointGeometry(34.814232150018626, 32.01469291408076),
        new PointGeometry(34.80350684441803, 32.01096849276168)));
    events.add(new MapDragEvent(new PointGeometry(34.80350684441803, 32.01096849276168),
        new PointGeometry(34.79867136146087, 32.009652398995065)));
    events.add(new MapDragEvent(new PointGeometry(34.79867136146087, 32.009652398995065),
        new PointGeometry(34.79327248309891, 32.00847530432637)));
    events.add(new MapDragEvent(new PointGeometry(34.79327248309891, 32.00847530432637),
        new PointGeometry(34.78881874180621, 32.00783809023177)));
    events.add(new MapDragEvent(new PointGeometry(34.78881874180621, 32.00783809023177),
        new PointGeometry(34.784632503363184, 32.00732021424652)));
    events.add(new MapDragEvent(new PointGeometry(34.784632503363184, 32.00732021424652),
        new PointGeometry(34.780437201889185, 32.00713378538337)));
    events.add(new MapDragEvent(new PointGeometry(34.780437201889185, 32.00713378538337),
        new PointGeometry(34.77712589124383, 32.00680590319601)));
    events.add(new MapDragEvent(new PointGeometry(34.77712589124383, 32.00680590319601),
        new PointGeometry(34.774971694843494, 32.006871476123415)));
    events.add(new MapDragEvent(new PointGeometry(34.774971694843494, 32.006871476123415),
        new PointGeometry(34.77126511704901, 32.006871476123415)));
    events.add(new MapDragEvent(new PointGeometry(34.77126511704901, 32.006871476123415),
        new PointGeometry(34.770175954286515, 32.006871476123415)));
    events.add(new MapDragEvent(new PointGeometry(34.770175954286515, 32.006871476123415),
        new PointGeometry(34.769515139267426, 32.006871476123415)));
    events.add(new MapDragEvent(new PointGeometry(34.769515139267426, 32.006871476123415),
        new PointGeometry(34.76912089210772, 32.006871476123415)));
    return events;
  }

  @Override
  public void lookAt(Geometry geometry) {

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