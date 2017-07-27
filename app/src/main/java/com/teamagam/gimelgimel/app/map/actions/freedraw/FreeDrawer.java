package com.teamagam.gimelgimel.app.map.actions.freedraw;

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
    mGgMapView.getMapDragEventObservable().subscribe(this::handleMapDragEvent);
  }

  public void undo() {
    if (!mCommands.isEmpty()) {
      mCommands.pop().undo();
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

  private void handleMapDragEvent(MapDragEvent mde) {
    if (!mDrawingEnabled) {
      return;
    }

    if (isNewDragStream(mde)) {
      mCurrentPoints = new ArrayList<>();
      mCurrentPoints.add(mde.getFrom());
    } else {
      undo();
    }
    mCurrentPoints.add(mde.getTo());
    PolylineEntity p = mMapEntityFactory.createPolyline(mCurrentPoints, mSymbol);
    addDisplayCommand(p);
  }

  private void addDisplayCommand(PolylineEntity p) {
    DisplayCommand command = new DisplayCommand(p);
    command.execute();
    mCommands.push(command);
  }

  private boolean isNewDragStream(MapDragEvent mde) {
    return mCurrentPoints == null || !lastPointConnectedToThisDragEvent(mde);
  }

  private boolean lastPointConnectedToThisDragEvent(MapDragEvent mde) {
    PointGeometry lastTo = mCurrentPoints.get(mCurrentPoints.size() - 1);
    return abs(mde.getFrom().getLatitude() - lastTo.getLatitude()) <= mTolerance
        && abs(mde.getFrom().getLongitude() - lastTo.getLongitude()) <= mTolerance;
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
      RemoveCommand command = new RemoveCommand(entity);
      command.execute();
      mCommands.push(command);
    }
  }
}
