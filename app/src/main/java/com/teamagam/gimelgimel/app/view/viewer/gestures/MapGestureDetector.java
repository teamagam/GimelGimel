package com.teamagam.gimelgimel.app.view.viewer.gestures;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Detects gestures over {@link GGMapView} and raises events on injected
 * {@link OnMapGestureListener}
 */
public class MapGestureDetector {

    /**
     * Underlying gesture detector used to catch gestures
     */
    private GestureDetector mGestureDetector;

    /**
     * Used as both detector context and last-touch-location port
     */
    private GGMapView mGGMapView;

    /**
     * Event raiser
     */
    private OnMapGestureListener mMapGestureListener;

    /**
     * Constructs a new instance of {@link MapGestureDetector}
     * instance needs to startDetecting() before it can raises gesture events on given listener
     *
     * @param ggMapView            - the touch events source for the detector
     * @param onMapGestureListener - the listener to invoke on gestures
     */
    public MapGestureDetector(GGMapView ggMapView, OnMapGestureListener onMapGestureListener) {
        mGGMapView = ggMapView;
        mGestureDetector = new GestureDetector(mGGMapView.getView().getContext(),
                new MyOnGestureListener());
        mMapGestureListener = onMapGestureListener;
    }

    /**
     * Registers this detector with initialized GGMapView to consume its touch events
     * for gestures processing
     */
    public void startDetecting() {
        mGGMapView.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    /**
     * Retrieves a copy of the last touched location for gesture events raising.
     *
     * @return copy of last touched location at call time
     */
    private PointGeometry getLastTouchLocationCopy() {
        return new PointGeometry(mGGMapView.getLastTouchedLocation());
    }

    /**
     * Listens for gestures and raises suiting event on MapGestureDetector
     * Doesn't raise multi-points gestures (scroll/fling)
     */
    private class MyOnGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            PointGeometry lastTouchLocation = getLastTouchLocationCopy();
            mMapGestureListener.onDown(lastTouchLocation);
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            PointGeometry lastTouchLocation = getLastTouchLocationCopy();
            mMapGestureListener.onShowPress(lastTouchLocation);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            PointGeometry lastTouchLocation = getLastTouchLocationCopy();
            mMapGestureListener.onSingleTapUp(lastTouchLocation);

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            PointGeometry lastTouchLocation = getLastTouchLocationCopy();
            mMapGestureListener.onLongPress(lastTouchLocation);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
