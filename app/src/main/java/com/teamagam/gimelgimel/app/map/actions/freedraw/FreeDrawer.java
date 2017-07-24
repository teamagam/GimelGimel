package com.teamagam.gimelgimel.app.map.actions.freedraw;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapDragEvent;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FreeDrawer {

  private GGMapView mGgMapView;
  private MapEntityFactory mMapEntityFactory;
  private Stack<GeoEntity> mDisplayedEntities;
  private List<PointGeometry> mCurrentPoints;
  private GeoEntity mCurrentGeoEntity;

  public FreeDrawer(GGMapView ggMapView, Observable<MapDragEvent> observable) {
    mGgMapView = ggMapView;
    mMapEntityFactory = new MapEntityFactory();
    mDisplayedEntities = new Stack<>();
    observable.subscribe(this::handleMapDragEvent);
  }

  public void undo() {
    removeLastEntity();
  }

  public void disable() {

  }

  public void enable() {

  }

  private void handleMapDragEvent(MapDragEvent mde) {
    if (isNewDragStream(mde)) {
      resetCurrent(mde);
    } else {
      removeLastEntity();
    }
    mCurrentPoints.add(mde.getTo());
    addEntityFromCurrent();
  }

  private boolean isNewDragStream(MapDragEvent mde) {
    return mCurrentPoints == null || !lastPointConnectedToThisDragEvent(mde);
  }

  private boolean lastPointConnectedToThisDragEvent(MapDragEvent mde) {
    PointGeometry lastTo = mCurrentPoints.get(mCurrentPoints.size() - 1);
    return mde.getFrom() == lastTo;
  }

  private void resetCurrent(MapDragEvent mde) {
    mCurrentGeoEntity = null;
    mCurrentPoints = new ArrayList<>();
    mCurrentPoints.add(mde.getFrom());
  }

  private void removeLastEntity() {
    if (!mDisplayedEntities.isEmpty()) {
      GeoEntity entity = mDisplayedEntities.pop();
      mGgMapView.updateMapEntity(GeoEntityNotification.createRemove(entity));
    }
  }

  private void addEntityFromCurrent() {
    mCurrentGeoEntity = mMapEntityFactory.createPolyline(mCurrentPoints);
    mGgMapView.updateMapEntity(GeoEntityNotification.createAdd(mCurrentGeoEntity));
    mDisplayedEntities.push(mCurrentGeoEntity);
  }
}
