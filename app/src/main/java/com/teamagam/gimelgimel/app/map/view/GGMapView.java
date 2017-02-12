package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;

/**
 * Exposes the implementing view object
 */
public interface GGMapView extends GGMap {

    /**
     * @param onMapGestureListener
     */
    void setGGMapGestureListener(OnMapGestureListener onMapGestureListener);
}

