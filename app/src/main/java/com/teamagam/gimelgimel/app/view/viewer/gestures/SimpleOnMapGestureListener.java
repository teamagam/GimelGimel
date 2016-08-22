package com.teamagam.gimelgimel.app.view.viewer.gestures;

import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;

/**
 * A convenience class to extend when you only want to listen for a subset of all the gestures.
 * It implements all of {@link OnMapGestureListener} methods but does nothing on events (empty
 * implementations)
 */
public class SimpleOnMapGestureListener implements OnMapGestureListener {
    @Override
    public void onDown(PointGeometry pointGeometry) {

    }

    @Override
    public void onShowPress(PointGeometry pointGeometry) {

    }

    @Override
    public void onSingleTapUp(PointGeometry pointGeometry) {

    }

    @Override
    public void onLongPress(PointGeometry pointGeometry) {

    }

    @Override
    public void onDoubleTap(PointGeometry pointGeometry) {

    }
}
