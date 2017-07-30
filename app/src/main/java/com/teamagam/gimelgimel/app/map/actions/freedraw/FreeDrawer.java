package com.teamagam.gimelgimel.app.map.actions.freedraw;

import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapDragEvent;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static java.lang.Math.abs;

public class FreeDrawer {

  protected AppLogger sLogger = AppLoggerFactory.create(this.getClass());

  private GGMapView mGgMapView;
  private double mTolerance;
  private MapEntityFactory mMapEntityFactory;
  private PolylineSymbol mSymbol;
  private Stack<Command> mCommands;
  private Map<String, GeoEntity> mEntityByIdMap;
  private boolean mDrawingEnabled;
  private List<PointGeometry> mCurrentPoints;

  public FreeDrawer(GGMapView ggMapView, String initialColor, double tolerance) {
    mGgMapView = ggMapView;
    mSymbol = createSymbol(initialColor);
    mTolerance = tolerance;
    mMapEntityFactory = new MapEntityFactory();
    mCommands = new Stack<>();
    mEntityByIdMap = new HashMap<>();
    mDrawingEnabled = true;
  }

  public void start() {
    sLogger.v("Started free drawing");
    mGgMapView.getMapDragEventObservable().doOnNext(this::log).subscribe(this::handleMapDragEvent);
  }

  public void undo() {
    if (!mCommands.isEmpty()) {
      mCommands.pop().undo();
    } else {
      sLogger.v("No action to undo");
    }
  }

  public void setColor(String color) {
    mSymbol = createSymbol(color);
  }

  public void switchMode() {
    toggle();
    mGgMapView.setOnEntityClickedListener(mDrawingEnabled ? null : new EraserListener());
  }

  public boolean isInEraserMode() {
    return !mDrawingEnabled;
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

  synchronized private void handleMapDragEvent(MapDragEvent mde) {
    if (mDrawingEnabled && containsDrag(mde)) {
      drawDragEvent(mde);
    }
  }

  private boolean containsDrag(MapDragEvent mde) {
    return !mde.getFrom().equals(mde.getTo());
  }

  private void drawDragEvent(MapDragEvent mde) {
    if (isNewDragStream(mde)) {
      resetStream(mde);
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

  private void resetStream(MapDragEvent mde) {
    mCurrentPoints = new ArrayList<>();
    mCurrentPoints.add(mde.getFrom());
  }

  private void hideLastDrawnEntity() {
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

  private void toggle() {
    mDrawingEnabled = !mDrawingEnabled;
  }

  private interface Command {
    void execute();

    void undo();
  }

  private abstract class MapActionCommand implements Command {

    private GeoEntity mEntity;

    private MapActionCommand(GeoEntity entity) {
      mEntity = entity;
    }

    protected void display() {
      mGgMapView.updateMapEntity(GeoEntityNotification.createAdd(mEntity));
      mEntityByIdMap.put(mEntity.getId(), mEntity);
    }

    protected void remove() {
      mGgMapView.updateMapEntity(GeoEntityNotification.createRemove(mEntity));
      mEntityByIdMap.remove(mEntity.getId());
    }
  }

  private class DisplayCommand extends MapActionCommand {

    private DisplayCommand(GeoEntity entity) {
      super(entity);
    }

    @Override
    public void execute() {
      display();
    }

    @Override
    public void undo() {
      remove();
    }
  }

  private class RemoveCommand extends MapActionCommand {

    private RemoveCommand(GeoEntity entity) {
      super(entity);
    }

    @Override
    public void execute() {
      remove();
    }

    @Override
    public void undo() {
      display();
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
