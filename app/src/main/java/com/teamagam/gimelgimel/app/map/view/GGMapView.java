package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.app.map.model.EntityUpdateEventArgs;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

/**
 * Defines all the functionality the apps needs from a map component
 */
public interface GGMapView {

    /**
     * Centers the viewer camera over given point, maintaining the current camera height
     *
     * @param point - uses only x,y of the point for zooming
     */
    void lookAt(PointGeometryApp point);

    /**
     * Centers the viewer's camera over given point, using the given height as the new camera's height
     *
     * @param point
     * @param cameraHeight
     */
    void lookAt(PointGeometryApp point, float cameraHeight);

    /**
     * updates the map with new/remove/update {@link Entity} using {@link EntityUpdateEventArgs}
     *
     * @param geoEntityNotification
     */
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
