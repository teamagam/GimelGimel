package com.teamagam.gimelgimel.app.view.viewer.gestures;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * TODO: add class summary notes
 */
public interface OnMapGestureListener {
    void onDown(PointGeometry pointGeometry);

    void onShowPress(PointGeometry pointGeometry);

    void onSingleTapUp(PointGeometry pointGeometry);

    void onLongPress(PointGeometry pointGeometry);
}
