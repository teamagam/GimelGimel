package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;

/**
 * TODO: add class summary notes
 */
public interface OnMapGestureListener {

    void onLocationChosen(PointGeometryApp pointGeometry);

    void onZoomRequested(PointGeometryApp pointGeometry);
}
