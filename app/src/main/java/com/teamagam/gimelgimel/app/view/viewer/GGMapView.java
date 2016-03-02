package com.teamagam.gimelgimel.app.view.viewer;

import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;

import java.util.Collection;

/**
 * Created by Bar on 29-Feb-16.
 */
public interface GGMapView {
    void addLayer(VectorLayer layer);
    void removeLayer(VectorLayer layer);
    Collection<VectorLayer> getLayers();
}
