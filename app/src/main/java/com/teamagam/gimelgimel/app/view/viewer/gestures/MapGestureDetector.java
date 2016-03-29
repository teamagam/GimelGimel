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
     * instance needs to register() before it can raises gesture events on given listener
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
    public void register() {
        mGGMapView.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    private PointGeometry getLastTouchLocation() {
        return mGGMapView.getLastTouchedLocation();
    }

    /**
     * Listens for gestures and raises suiting event on MapGestureDetector
     * Doesn't raise multi-points gestures (scroll/fling)
     */
    private class MyOnGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            PointGeometry lastTouchLocation = getLastTouchLocation();
            mMapGestureListener.onDown(lastTouchLocation);
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            PointGeometry lastTouchLocation = getLastTouchLocation();
            mMapGestureListener.onShowPress(lastTouchLocation);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            PointGeometry lastTouchLocation = getLastTouchLocation();
            mMapGestureListener.onSingleTapUp(lastTouchLocation);

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            PointGeometry lastTouchLocation = getLastTouchLocation();
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
