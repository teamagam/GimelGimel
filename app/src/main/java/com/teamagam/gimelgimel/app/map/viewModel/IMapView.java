package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.app.map.model.VectorLayer;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * Created on 8/18/2016.
 * TODO: complete text
 */
public interface IMapView {
    void takePicture();

    void goToLocation(PointGeometry location);

    void addLayer(VectorLayer vectorLayer);
}
