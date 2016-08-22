package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.view.View;

import com.teamagam.gimelgimel.app.map.cesium.OnGGMapReadyListener;
import com.teamagam.gimelgimel.app.map.view.gestures.OnMapGestureListener;

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
     * @param listener - custom listener to add
     */
    void setOnReadyListener(OnGGMapReadyListener listener);

    /**
     *
     * @return true iff the GGMap is ready for actions
     */
    boolean isReady();

    /**
     * Save the state of the view, a state can be restored by {@code saveViewState}.
     * @param outState a {@link Bundle} object to store the data in it.
     */
    void saveViewState(Bundle outState);

    /**
     * Restore the state of the view, an put it back where it was before.
     * @param inState a {@link Bundle} object to retrieve the data from it.
     */
    void restoreViewState(Bundle inState);


    /**
     *
     * @param onMapGestureListener
     */
    void setGGMapGestureListener(OnMapGestureListener onMapGestureListener );
}

