package com.teamagam.gimelgimel.app.view.viewer;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.view.viewer.data.GGLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Collection;

/**
 * Created by Bar on 29-Feb-16.
 * <p/>
 * Defines all the functionality the apps needs from a viewer
 */
public interface GGMapView {
    /***
     * Adds and displays given {@link GGLayer} on the viewer.
     * Any changes to the a layer's
     * {@link com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity} should immediately be reflected on the viewer.
     *
     * @param layer the vector layer to present on the viewer
     */
    void addLayer(GGLayer layer);

    /***
     * Removes given {@link GGLayer} from presentation
     * @param layer to be removed
     */
    void removeLayer(GGLayer layer);

    /***
     * @return all of the {@link GGLayer}s the viewer holds
     */
    Collection<GGLayer> getLayers();

    /**
     * Fly to a Rectangle with a top-down view
     * @param west
     * @param south
     * @param east
     * @param north
     */
    void setExtent(float west,float south,float east, float north);

    /**
     * Zooms the map to the given entity(s) so that entity(s) fits within the bounds of the map
     * @param entities
     */
    void setExtent(Collection<Entity> entities);

    void zoomTo(float longitude, float latitude, float altitude);

    void zoomTo(float longitude, float latitude);

    void readAsyncCenterPosition(ValueCallback<PointGeometry> callback);

}
