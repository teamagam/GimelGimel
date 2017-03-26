package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;

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

    interface OnReadyListener {
        void onReady();
    }
}
