package com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces;


import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.teamagam.gimelgimel.app.common.SynchronizedDataHolder;
import com.teamagam.gimelgimel.app.view.viewer.cesium.CesiumUtils;
import com.teamagam.gimelgimel.app.view.viewer.cesium.bridges.CesiumGestureBridge;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.gestures.OnMapGestureListener;

import org.xwalk.core.JavascriptInterface;

/**
 * A {@link org.xwalk.core.JavascriptInterface} methods class
 * object with currently selected location update functionality
 */
public class CesiumMapGestureDetector extends GestureDetector.SimpleOnGestureListener{

    public static final String JAVASCRIPT_INTERFACE_NAME = "CesiumMapGestureDetector";

    private SynchronizedDataHolder<PointGeometry> mViewedLocationHolder;
    private OnMapGestureListener mOnMapGestureListener;
    private final CesiumGestureBridge mCesiumGestureBridge;

    public CesiumMapGestureDetector(View view, CesiumGestureBridge cesiumGestureBridge) {
        mViewedLocationHolder = new SynchronizedDataHolder<>(PointGeometry.DEFAULT_POINT);
        mCesiumGestureBridge = cesiumGestureBridge;
        setGestureListener(view);
    }

    /***
     * A method exposed for javascript execution.
     * Updates selected location
     *
     * @param locationJson - An json with latitude and longitude
     *                     properties describing current location
     */
    @JavascriptInterface
    public void onLongPress(String locationJson) {
        PointGeometry pg = CesiumUtils.getPointGeometryFromJson(locationJson);
        mOnMapGestureListener.onLongPress(pg);
    }


    @JavascriptInterface
    public void onDoubleTap(String locationJson) {
        PointGeometry pg = CesiumUtils.getPointGeometryFromJson(locationJson);
        mOnMapGestureListener.onDoubleTap(pg);
    }

    @JavascriptInterface
    public void updateViewedLocation(String locationJson) {
        PointGeometry pg = CesiumUtils.getPointGeometryFromJson(locationJson);
        mViewedLocationHolder.setData(pg);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        mCesiumGestureBridge.onDoubleTap();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        mCesiumGestureBridge.onLongPress();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mCesiumGestureBridge.onSingleTap();
        return true;
    }

    public PointGeometry getLastViewedLocation() {
        return mViewedLocationHolder.getData();
    }

    public void setOnMapGestureListener(OnMapGestureListener onMapGestureListener) {
        mOnMapGestureListener = onMapGestureListener;
    }

    private void setGestureListener(View view){
        final GestureDetector gestureDetector = new GestureDetector(view.getContext(), this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

}
