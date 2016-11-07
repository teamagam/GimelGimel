package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GGMap;
import com.teamagam.gimelgimel.app.map.viewModel.IMapView;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

public class GGMapGestureListener extends SimpleOnMapGestureListener {

    private static final Logger sLogger = LoggerFactory.create(GGMapGestureListener.class);

    private GGMap mGGMap;
    private IMapView mMapView;

    public GGMapGestureListener(GGMap ggMap, IMapView mapView) {
        mGGMap = ggMap;
        mMapView = mapView;
    }

    @Override
    public void onLocationChosen(PointGeometryApp pointGeometry) {
        /** create send geo message dialog **/
        mMapView.openSendGeoDialog(pointGeometry);
    }

    @Override
    public void onZoomRequested(final PointGeometryApp pointGeometry) {
        sLogger.userInteraction("double-tap");
        zoomInTo(pointGeometry);
    }

    private void zoomInTo(PointGeometryApp pointGeometry) {
        pointGeometry.altitude = getCurrentHeight() * Constants.ZOOM_IN_FACTOR;
        mGGMap.zoomTo(pointGeometry);
    }

    private double getCurrentHeight() {
        return mGGMap.getLastViewedLocation().altitude;
    }

}
