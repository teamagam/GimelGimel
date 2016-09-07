package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * TODO: add class summary notes
 */
public interface OnMapGestureListener {

    @SuppressWarnings("unused")
    void onDown(PointGeometry pointGeometry);

    @SuppressWarnings("unused")
    void onShowPress(PointGeometry pointGeometry);

    @SuppressWarnings("unused")
    void onSingleTapUp(PointGeometry pointGeometry);

    void onLocationChosen(PointGeometry pointGeometry);

    void onZoomRequested(PointGeometry pointGeometry);
}
