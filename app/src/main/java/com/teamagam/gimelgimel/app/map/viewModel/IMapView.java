package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.app.map.model.EntityUpdateEventArgs;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

/**
 * connects ViewModel of the map view with it's view
 * ({@link com.teamagam.gimelgimel.app.map.view.ViewerFragment}.
 */
public interface IMapView {

    void lookAt(PointGeometryApp location);

    void lookAt(PointGeometryApp location, float height);

    void setCameraPosition(ViewerCamera viewerCamera);

    void addLayer(String layerId);

    rx.Observable<ViewerCamera> getViewerCameraObservable();

    void updateMapEntity(EntityUpdateEventArgs entityUpdateEventArgs);

    void showVectorLayer(VectorLayerPresentation vectorLayerPresentation);

    void hideVectorLayer(String vectorLayerId);
}

