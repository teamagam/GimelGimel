package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.viewModel.IMapView;
import com.teamagam.gimelgimel.app.map.viewModel.MapViewModel;
import com.teamagam.gimelgimel.app.utils.Constants;

public class GGMapGestureListener extends SimpleOnMapGestureListener {

    private static final AppLogger sLogger = AppLoggerFactory.create(GGMapGestureListener.class);

    private MapViewModel mMapViewModel;
    private IMapView mMapView;

    public GGMapGestureListener(MapViewModel mapViewModel, IMapView mapView) {
        mMapViewModel = mapViewModel;
        mMapView = mapView;
    }

    @Override
    public void onLocationChosen(PointGeometryApp pointGeometry) {
        /** create send geo message dialog **/
        mMapViewModel.openSendGeoDialog(pointGeometry);
    }

    @Override
    public void onZoomRequested(final PointGeometryApp pointGeometry) {
        sLogger.userInteraction("double-tap");
        zoomInTo(pointGeometry);
    }

    private void zoomInTo(PointGeometryApp pointGeometry) {
        float newHeight = (float) (getCurrentHeight() * Constants.ZOOM_IN_FACTOR);
        mMapView.lookAt(pointGeometry, newHeight);
    }

    private double getCurrentHeight() {
        return mMapViewModel.getViewerCamera().cameraPosition.getAltitude();
    }
}
