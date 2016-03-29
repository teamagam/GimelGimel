package com.teamagam.gimelgimel.app.view.viewer.listeners;

import android.view.MotionEvent;
import android.view.View;

import com.teamagam.gimelgimel.app.view.viewer.GGMap;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Simplifies work with map screen-space events and their associated map spatial location.
 * <p/>
 * Implements all the needed (add if necessary) screen-space event-listeners
 */
public abstract class MapScreenSpaceEventListener implements
        View.OnClickListener, View.OnLongClickListener, View.OnHoverListener, View.OnTouchListener {

    private GGMap mGGMap;

    public MapScreenSpaceEventListener(GGMap ggMap) {
        this.mGGMap = ggMap;
    }

    @Override
    public void onClick(View v) {
        handleScreenSpaceEvent();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        handleScreenSpaceEvent();

        //Indicates listener doesn't consumes the event
        //(passes event to next on-touch-listener in line)
        return false;
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        handleScreenSpaceEvent();

        //Indicates listener doesn't consumes the event
        // (passes event to next hover-listener in line)
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        handleScreenSpaceEvent();

        //Indicates listener doesn't consumes the event (passes event to next long_click-listener in line)
        return false;
    }

    protected abstract void handleEvent(PointGeometry lastTouchedLocation);

    private void handleScreenSpaceEvent() {
        PointGeometry pg = mGGMap.getLastTouchedLocation();
        handleEvent(pg);
    }
}
