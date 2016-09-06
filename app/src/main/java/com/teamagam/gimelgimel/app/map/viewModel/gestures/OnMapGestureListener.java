package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * TODO: add class summary notes
 */
public interface OnMapGestureListener {

    void onLocationChosen(PointGeometry pointGeometry);

    void onZoomRequested(PointGeometry pointGeometry);
}
