package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.app.map.model.EntityUpdateEventArgs;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import rx.Observable;

/*
TODO: consider splitting functionality to different interfaces that GGMap will extend
*/


/**
 * Defines all the functionality the apps needs from a map component
 */
public interface GGMap {

    /***
     * Adds and displays given {@link String} on the viewer.
     * Any changes to the a layer's
     * {@link Entity} should immediately be reflected on the viewer.
     *
     * @param layerId the vector layer to present on the viewer
     */
    void addLayer(String layerId);

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
     * Sets the viewer's camera state (position + looking direction)
     *
     * @param viewerCamera
     */
    void setCameraPosition(ViewerCamera viewerCamera);

    /**
     * Gets an observable that emits viewer camera changes events
     *
     * @return
     */
    Observable<ViewerCamera> getViewerCameraObservable();

    /**
     * updates the map with new/remove/update {@link Entity} using {@link EntityUpdateEventArgs}
     *
     * @param eventArgs
     */
    void updateMapEntity(EntityUpdateEventArgs eventArgs);

    void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener);

    void showVectorLayer(VectorLayerPresentation vectorLayerPresentation);

    void hideVectorLayer(String vectorLayerId);

    void setOnReadyListener(OnReadyListener onReadyListener);

    interface OnReadyListener {
        void onReady();
    }
}
