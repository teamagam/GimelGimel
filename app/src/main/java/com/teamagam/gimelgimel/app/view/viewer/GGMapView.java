package com.teamagam.gimelgimel.app.view.viewer;

import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;

import java.util.Collection;

/**
 * Created by Bar on 29-Feb-16.
 * <p/>
 * Defines all the functionality the apps needs from a viewer
 */
public interface GGMapView {
    /***
     * Adds and displays given {@link VectorLayer} on the viewer.
     * Any changes to the a layer's
     * {@link com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity} should immediately be reflected on the viewer.
     *
     * @param layer the vector layer to present on the viewer
     */
    void addLayer(VectorLayer layer);

    /***
     * Removes given {@link VectorLayer} from presentation
     * @param layer to be removed
     */
    void removeLayer(VectorLayer layer);

    /***
     * @return all of the {@link VectorLayer}s the viewer holds
     */
    Collection<VectorLayer> getLayers();
}
