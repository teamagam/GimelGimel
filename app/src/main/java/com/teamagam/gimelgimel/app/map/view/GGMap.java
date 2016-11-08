package com.teamagam.gimelgimel.app.map.view;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.map.cesium.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.model.EntityUpdateEventArgs;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
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

    /***
     * Removes layer associated with given id from presentation, if there is any.
     *
     * @param layerId to be removed
     */
    void removeLayer(String layerId);

    /**
     * Fly to a Rectangle with a top-down view
     *
     * @param west
     * @param south
     * @param east
     * @param north
     */
    void setExtent(float west, float south, float east, float north);

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

    void readAsyncCenterPosition(ValueCallback<PointGeometryApp> callback);

    /**
     * Gets an observable that emits viewer camera changes events
     *
     * @return
     */
    Observable<ViewerCamera> getViewerCameraObservable();

    /**
     * updates the map with new/remove/update {@link Entity} using {@link EntityUpdateEventArgs}
     * @param eventArgs
     */
    void updateMapEntity(EntityUpdateEventArgs eventArgs);

    void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener);
}
