package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;

/**
 * A convenience class to extend when you only want to listen for a subset of all the gestures.
 * It implements all of {@link OnMapGestureListener} methods but does nothing on events (empty
 * implementations)
 */
public class SimpleOnMapGestureListener implements OnMapGestureListener {

    @Override
    public void onLocationChosen(PointGeometryApp pointGeometry) {

    }

    @Override
    public void onZoomRequested(PointGeometryApp pointGeometry) {

    }
}
