package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.app.map.model.VectorLayer;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

/**
 * connects ViewModel of the map view with it's view
 * ({@link com.teamagam.gimelgimel.app.map.view.ViewerFragment}.
 */
public interface IMapView {

    void lookAt(PointGeometryApp location);

    void lookAt(PointGeometryApp location, float height);

    void setCameraPosition(ViewerCamera viewerCamera);

    void addLayer(VectorLayer vectorLayer);

    rx.Observable<ViewerCamera> getViewerCameraObservable();
}

