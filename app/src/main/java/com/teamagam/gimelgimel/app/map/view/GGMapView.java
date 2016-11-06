package com.teamagam.gimelgimel.app.map.view;

import android.view.View;

import com.teamagam.gimelgimel.app.map.cesium.OnGGMapReadyListener;
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
     * Custom listener registration to GGMapView ready event.
     * A GGMapView is ready when it is able to perform GGMap operations on screen
     *
     * @param listener - custom listener to add
     */
    void setOnReadyListener(OnGGMapReadyListener listener);

    /**
     * @return true iff the GGMap is ready for actions
     */
    boolean isReady();


    /**
     * @param onMapGestureListener
     */
    void setGGMapGestureListener(OnMapGestureListener onMapGestureListener);
}

