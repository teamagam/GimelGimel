package com.teamagam.gimelgimel.app.view.viewer;

import android.view.View;

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
}

