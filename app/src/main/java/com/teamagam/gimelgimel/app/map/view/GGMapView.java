package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;

public interface GGMapView {

    void lookAt(PointGeometryApp point);

    void lookAt(PointGeometryApp point, float cameraHeight);

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

    interface OnReadyListener {
        void onReady();
    }
}
