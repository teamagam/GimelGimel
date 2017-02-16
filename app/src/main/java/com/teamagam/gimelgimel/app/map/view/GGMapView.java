package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

public interface GGMapView {

    void lookAt(PointGeometryApp point);

    void lookAt(PointGeometryApp point, float cameraHeight);

    void updateMapEntity(GeoEntityNotification geoEntityNotification);

    void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener);

    void showVectorLayer(VectorLayerPresentation vectorLayerPresentation);

    void hideVectorLayer(String vectorLayerId);

    void setOnReadyListener(OnReadyListener onReadyListener);

    void setGGMapGestureListener(OnMapGestureListener onMapGestureListener);

    void saveState();

    void restoreState();

    interface OnReadyListener {
        void onReady();
    }
}
