package com.teamagam.gimelgimel.app.map.view.gestures;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * TODO: add class summary notes
 */
public interface OnMapGestureListener {
    void onDown(PointGeometry pointGeometry);

    void onShowPress(PointGeometry pointGeometry);

    void onSingleTapUp(PointGeometry pointGeometry);

    void onLongPress(PointGeometry pointGeometry);

    void onDoubleTap(PointGeometry pointGeometry);
}
