package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

/**
 * connects ViewModel of the map view with it's view
 * ({@link com.teamagam.gimelgimel.app.map.view.ViewerFragment}.
 */
public interface IMapView {

    void lookAt(PointGeometryApp location);

    void lookAt(PointGeometryApp location, float height);

    void updateMapEntity(GeoEntityNotification geoEntityNotification);

    void showVectorLayer(VectorLayerPresentation vectorLayerPresentation);

    void hideVectorLayer(String vectorLayerId);
}

