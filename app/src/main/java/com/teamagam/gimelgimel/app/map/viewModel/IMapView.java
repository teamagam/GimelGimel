package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.app.map.model.VectorLayer;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * connects ViewModel of the map view with it's view
 * ({@link com.teamagam.gimelgimel.app.map.view.ViewerFragment}.
 */
public interface IMapView {

    void goToLocation(PointGeometry location);

    void addLayer(VectorLayer vectorLayer);

    void openSendGeoDialog(PointGeometry pointGeometry);
}

