package com.teamagam.gimelgimel.app.map.view;

import android.view.View;

import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;

/**
 * Exposes the implementing view object
 */
public interface GGMapView extends GGMap {

    /**
     * @return the implementing GGMap {@link View}
     */
    View getView();

    /**
     * @return true iff the GGMap is ready for actions
     */
    boolean isReady();


    /**
     * @param onMapGestureListener
     */
    void setGGMapGestureListener(OnMapGestureListener onMapGestureListener);
}

