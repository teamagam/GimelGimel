package com.teamagam.gimelgimel.app.view.viewer;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.view.viewer.data.GGLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Collection;

/*
TODO: consider splitting functionality to different interfaces that GGMap will extend
*/


/**
 * Defines all the functionality the apps needs from a map component
 */
public interface GGMap {
    /***
     * Adds and displays given {@link GGLayer} on the viewer.
     * Any changes to the a layer's
     * {@link com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity} should immediately be reflected on the viewer.
     *
     * @param layer the vector layer to present on the viewer
     */
    void addLayer(GGLayer layer);

    /***
     * Removes layer associated with given id from presentation, if there is any.
     *
     * @param layerId to be removed
     */
    void removeLayer(String layerId);

    /***
     * @return all of the {@link GGLayer}s the viewer holds
     */
    Collection<GGLayer> getLayers();

    /***
     * Gets a {@link GGLayer} by id
     *
     * @param id wanted layer id to retrieve
     * @return {@link GGLayer} matching given id, if it exists. otherwise, returns null
     */
    GGLayer getLayer(String id);

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
     * Zooms the map to the given entity(s) so that entity(s) fits within the bounds of the map
     *
     * @param entities
     */
    void setExtent(Collection<Entity> entities);

    //TODO: add documentation to interface methods

    void zoomTo(float longitude, float latitude, float altitude);

    void zoomTo(float longitude, float latitude);

    void zoomTo(PointGeometry point);

    void readAsyncCenterPosition(ValueCallback<PointGeometry> callback);

    /**
     * Returns the last viewed location that the user saw on the map.
     * @return The last viewed location (The center point).
     */
    PointGeometry getLastViewedLocation();
}
