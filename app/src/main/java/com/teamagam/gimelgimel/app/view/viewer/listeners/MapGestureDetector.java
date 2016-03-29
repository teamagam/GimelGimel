package com.teamagam.gimelgimel.app.view.viewer.listeners;

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
     * Registers given detector to consume touch events from given view
     *
     * @param ggMapView - consumes touch event from (in-order to process gesture events)
     * @param detector  - registers as touch events consumer
     */
    public static void registerDetectorWithGGMapView(GGMapView ggMapView,
                                                     final MapGestureDetector detector) {
        ggMapView.getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }

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

    public MapGestureDetector(GGMapView ggMapView, OnMapGestureListener onMapGestureListener) {
        mGGMapView = ggMapView;
        mGestureDetector = new GestureDetector(mGGMapView.getView().getContext(),
                new MyOnGestureListener());
        mMapGestureListener = onMapGestureListener;
    }

    /**
     * Forwards on touch events to inner gesture detector.
     * see GestureDetector docs for more information
     */
    protected boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
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
