package com.teamagam.gimelgimel.app.map.actions.freedraw;

import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapDragEvent;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.teamagam.gimelgimel.domain.config.Constants.SIGNAL;
import static java.lang.Math.abs;

public class FreeDrawer {

  protected AppLogger sLogger = AppLoggerFactory.create(this.getClass());
  private GGMapView mGgMapView;

  private double mTolerance;
  private MapEntityFactory mMapEntityFactory;
  private PolylineSymbol mSymbol;
  private Stack<Command> mCommands;
  private Map<String, GeoEntity> mEntityByIdMap;
  private boolean mIsDrawingMode;
  private boolean mIsEnabled;
  private List<PointGeometry> mCurrentPoints;
  private SubjectRepository<Object> mOnStartDrawingSubject;

  public FreeDrawer(GGMapView ggMapView, String initialColor, double tolerance) {
    mGgMapView = ggMapView;
    mSymbol = createSymbol(initialColor);
    mTolerance = tolerance;
    mMapEntityFactory = new MapEntityFactory();
    mCommands = new Stack<>();
    mEntityByIdMap = new HashMap<>();
    mIsDrawingMode = true;
    mOnStartDrawingSubject = SubjectRepository.createSimpleSubject();
  }

  public void start() {
    mIsEnabled = true;
    sLogger.v("Started free drawing");
    mGgMapView.getMapDragEventObservable()
        .filter(mde -> mIsEnabled && mIsDrawingMode)
        .doOnNext(this::log)
        .filter(this::containsDrag)
        .subscribe(this::drawDragEvent);
  }

  public void undo() {
    if (!mCommands.isEmpty()) {
      undoLastCommand();
    } else {
      sLogger.v("No action to undo");
    }
  }

  public void setColor(String color) {
    mSymbol = createSymbol(color);
  }

  public void switchMode() {
    mIsDrawingMode = !mIsDrawingMode;
    mGgMapView.setOnEntityClickedListener(mIsDrawingMode ? null : new EraserListener());
  }

  public boolean isInEraserMode() {
    return !mIsDrawingMode;
  }

  public void enable() {
    mIsEnabled = true;
  }

  public void disable() {
    mIsEnabled = false;
  }

  public Collection<GeoEntity> getEntities() {
    return mEntityByIdMap.values();
  }

  public Observable<Object> getSignalOnStartDrawingObservable() {
    return mOnStartDrawingSubject.getObservable();
  }

  private void log(MapDragEvent mde) {
    sLogger.v("drag-event: "
        + mde.getFrom().getLongitude()
        + ","
        + mde.getFrom().getLatitude()
        + " - "
        + mde.getTo().getLongitude()
        + ","
        + mde.getTo().getLatitude());
  }

  private boolean containsDrag(MapDragEvent mde) {
    return !mde.getFrom().equals(mde.getTo());
  }

  private void drawDragEvent(MapDragEvent mde) {
    if (isNewDragStream(mde)) {
      resetStream(mde);
      signalOnStartDrawing();
    } else {
      hideLastDrawnEntity();
    }
    mCurrentPoints.add(mde.getTo());
    displayCurrent();
  }

  private boolean isNewDragStream(MapDragEvent mde) {
    return mCurrentPoints == null || !isConnectedToCurrentStream(mde);
  }

  private boolean isConnectedToCurrentStream(MapDragEvent mde) {
    PointGeometry lastTo = mCurrentPoints.get(mCurrentPoints.size() - 1);
    PointGeometry currentFrom = mde.getFrom();
    return abs(currentFrom.getLatitude() - lastTo.getLatitude()) <= mTolerance
        && abs(currentFrom.getLongitude() - lastTo.getLongitude()) <= mTolerance;
  }

  private void signalOnStartDrawing() {
    mOnStartDrawingSubject.add(SIGNAL);
  }

  private void resetStream(MapDragEvent mde) {
    mCurrentPoints = new ArrayList<>();
    mCurrentPoints.add(mde.getFrom());
  }

  private void hideLastDrawnEntity() {
    undoLastCommand();
  }

  private void undoLastCommand() {
    mCommands.pop().undo();
  }

  private void displayCurrent() {
    PolylineEntity p = mMapEntityFactory.createPolyline(mCurrentPoints, mSymbol);
    addDisplayCommand(p);
  }

  private void addDisplayCommand(PolylineEntity p) {
    DisplayCommand cmd = new DisplayCommand(p);
    cmd.execute();
    mCommands.push(cmd);
  }

  private PolylineSymbol createSymbol(String color) {
    return new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(color).build();
  }

  private void display(GeoEntity entity) {
    mGgMapView.updateMapEntity(GeoEntityNotification.createAdd(entity));
    mEntityByIdMap.put(entity.getId(), entity);
  }

  private void remove(GeoEntity entity) {
    mGgMapView.updateMapEntity(GeoEntityNotification.createRemove(entity));
    mEntityByIdMap.remove(entity.getId());
  }

  private interface Command {
    void execute();

    void undo();
  }

  private class DisplayCommand implements Command {

    private final GeoEntity mEntity;

    private DisplayCommand(GeoEntity entity) {
      mEntity = entity;
    }

    @Override
    public void execute() {
      display(mEntity);
    }

    @Override
    public void undo() {
      remove(mEntity);
    }
  }

  private class RemoveCommand implements Command {

    private final GeoEntity mEntity;

    private RemoveCommand(GeoEntity entity) {
      mEntity = entity;
    }

    @Override
    public void execute() {
      remove(mEntity);
    }

    @Override
    public void undo() {
      display(mEntity);
    }
  }

  private class EraserListener implements MapEntityClickedListener {
    @Override
    public void entityClicked(String entityId) {
      GeoEntity entity = mEntityByIdMap.remove(entityId);
      if (entity != null) {
        addRemoveCommand(entity);
      }
    }

    @Override
    public void kmlEntityClicked(KmlEntityInfo kmlEntityInfo) {
      // Nothing
    }

    private void addRemoveCommand(GeoEntity entity) {
      RemoveCommand cmd = new RemoveCommand(entity);
      cmd.execute();
      mCommands.push(cmd);
    }
  }
}
