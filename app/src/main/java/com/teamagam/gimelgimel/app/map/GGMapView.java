package com.teamagam.gimelgimel.app.map;

import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import io.reactivex.Observable;

public interface GGMapView {

  void lookAt(Geometry geometry);

  void updateMapEntity(GeoEntityNotification geoEntityNotification);

  void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener);

  void showVectorLayer(VectorLayerPresentation vectorLayerPresentation);

  void hideVectorLayer(String vectorLayerId);

  void setOnReadyListener(OnReadyListener onReadyListener);

  void setOnMapGestureListener(OnMapGestureListener onMapGestureListener);

  void saveState();

  void restoreState();

  void centerOverCurrentLocationWithAzimuth();

  void setIntermediateRaster(IntermediateRaster intermediateRaster);

  void removeIntermediateRaster();

  PointGeometry getMapCenter();

  Observable<MapDragEvent> getMapDragEventObservable();

  void setAllowPanning(boolean allow);

  interface OnReadyListener {
    void onReady();
  }
}
