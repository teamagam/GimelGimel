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

public class FreeDrawer {

  private final MapEntityFactory mMapEntityFactory;
  private List<PointGeometry> mCurrentPoints;
  private GGMapView mGgMapView;
  private GeoEntity mCurrentGeoEntity;

  public FreeDrawer(GGMapView ggMapView, Observable<MapDragEvent> observable) {
    mGgMapView = ggMapView;
    mMapEntityFactory = new MapEntityFactory();
    observable.subscribe(this::handleMapDragEvent);
  }

  public void undo() {

  }

  public void disable() {

  }

  public void enable() {

  }

  private void handleMapDragEvent(MapDragEvent mde) {
    resetOnNewDragStream(mde);
    mCurrentPoints.add(mde.getTo());
    updateEntityOnMap();
  }

  private void resetOnNewDragStream(MapDragEvent mde) {
    if (isNewDragStream(mde)) {
      mCurrentGeoEntity = null;
      mCurrentPoints = new ArrayList<>();
      mCurrentPoints.add(mde.getFrom());
    }
  }

  private boolean isNewDragStream(MapDragEvent mde) {
    return mCurrentPoints == null || mde.getFrom() != mCurrentPoints.get(mCurrentPoints.size() - 1);
  }

  private void updateEntityOnMap() {
    removeOldEntity();
    addNewEntity();
  }

  private void removeOldEntity() {
    if (mCurrentGeoEntity != null) {
      mGgMapView.updateMapEntity(GeoEntityNotification.createRemove(mCurrentGeoEntity));
    }
  }

  private void addNewEntity() {
    mCurrentGeoEntity = mMapEntityFactory.createPolyline(mCurrentPoints);
    mGgMapView.updateMapEntity(GeoEntityNotification.createAdd(mCurrentGeoEntity));
  }
}
