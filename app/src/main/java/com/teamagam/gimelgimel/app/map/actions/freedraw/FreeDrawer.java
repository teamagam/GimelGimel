package com.teamagam.gimelgimel.app.map.actions.freedraw;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapDragEvent;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import io.reactivex.Observable;
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
  private Stack<GeoEntity> mDisplayedEntities;
  private Map<String, GeoEntity> mEntityMap;
  private boolean mDrawingEnabled;
  private List<PointGeometry> mCurrentPoints;
  private GeoEntity mCurrentGeoEntity;

  public FreeDrawer(GGMapView ggMapView,
      Observable<MapDragEvent> observable,
      String initialColor,
      double tolerance) {
    mGgMapView = ggMapView;
    mSymbol = createSymbol(initialColor);
    mTolerance = tolerance;
    mMapEntityFactory = new MapEntityFactory();
    mDisplayedEntities = new Stack<>();
    mEntityMap = new HashMap<>();
    mDrawingEnabled = true;
    observable.subscribe(this::handleMapDragEvent);
  }

  public void undo() {
    removeLastEntity();
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
    if (mDrawingEnabled) {
      if (isNewDragStream(mde)) {
        resetCurrent(mde);
      } else {
        removeLastEntity();
      }
      mCurrentPoints.add(mde.getTo());
      addEntityFromCurrent();
    }
  }

  private boolean isNewDragStream(MapDragEvent mde) {
    return mCurrentPoints == null || !lastPointConnectedToThisDragEvent(mde);
  }

  private boolean lastPointConnectedToThisDragEvent(MapDragEvent mde) {
    PointGeometry lastTo = mCurrentPoints.get(mCurrentPoints.size() - 1);
    return abs(mde.getFrom().getLatitude() - lastTo.getLatitude()) <= mTolerance
        && abs(mde.getFrom().getLongitude() - lastTo.getLongitude()) <= mTolerance;
  }

  private void resetCurrent(MapDragEvent mde) {
    mCurrentGeoEntity = null;
    mCurrentPoints = new ArrayList<>();
    mCurrentPoints.add(mde.getFrom());
  }

  private void removeLastEntity() {
    if (!mDisplayedEntities.isEmpty()) {
      GeoEntity entity = mDisplayedEntities.pop();
      mEntityMap.remove(entity.getId());
      mGgMapView.updateMapEntity(GeoEntityNotification.createRemove(entity));
    }
  }

  private void addEntityFromCurrent() {
    mCurrentGeoEntity = mMapEntityFactory.createPolyline(mCurrentPoints, mSymbol);
    mGgMapView.updateMapEntity(GeoEntityNotification.createAdd(mCurrentGeoEntity));
    mDisplayedEntities.push(mCurrentGeoEntity);
    mEntityMap.put(mCurrentGeoEntity.getId(), mCurrentGeoEntity);
  }

  private PolylineSymbol createSymbol(String color) {
    return new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(color).build();
  }

  private void toggle() {
    mDrawingEnabled = !mDrawingEnabled;
  }

  private class EraserListener implements MapEntityClickedListener {
    @Override
    public void entityClicked(String entityId) {
      GeoEntity entity = mEntityMap.remove(entityId);
      if (entity != null) {
        mDisplayedEntities.remove(entity);
        mGgMapView.updateMapEntity(GeoEntityNotification.createRemove(entity));
      }
    }

    @Override
    public void kmlEntityClicked(KmlEntityInfo kmlEntityInfo) {
      // Never called
    }
  }
}
