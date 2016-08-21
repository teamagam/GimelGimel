package com.teamagam.gimelgimel.app.map;

import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Created on 8/18/2016.
 * TODO: complete text
 */
public interface IMapView {
    void takePicture();

    void goToLocation(PointGeometry location);

    void addLayer(VectorLayer vectorLayer);
}
