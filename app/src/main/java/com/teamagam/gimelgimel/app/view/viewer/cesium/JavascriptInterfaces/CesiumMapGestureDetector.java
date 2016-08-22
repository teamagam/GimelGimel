package com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces;


import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.teamagam.gimelgimel.app.common.SynchronizedDataHolder;
import com.teamagam.gimelgimel.app.view.viewer.cesium.CesiumUtils;
import com.teamagam.gimelgimel.app.view.viewer.cesium.bridges.CesiumGestureBridge;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
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
    private int mViewWidth;
    private int mViewHeight;

    public CesiumMapGestureDetector(View view, CesiumGestureBridge cesiumGestureBridge) {
        mViewedLocationHolder = new SynchronizedDataHolder<>(PointGeometry.DEFAULT_POINT);
        mCesiumGestureBridge = cesiumGestureBridge;
        setGestureListener(view);
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        mCesiumGestureBridge.onDoubleTap(event.getX() / mViewWidth, event.getY() / mViewHeight);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        mCesiumGestureBridge.onLongPress(event.getX() / mViewWidth, event.getY() / mViewHeight);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        mCesiumGestureBridge.onSingleTap(event.getX() / mViewWidth, event.getY() / mViewHeight);
        return true;
    }

    /***
     * A method exposed for javascript execution.
     * Updates selected location
     *
     * @param locationJson - An json with latitude and longitude
     *                     properties describing current location
     */
    @JavascriptInterface
    public void onLongPressJSResponse(String locationJson) {
        PointGeometry pg = CesiumUtils.getPointGeometryFromJson(locationJson);
        mOnMapGestureListener.onLongPress(pg);
    }


    @JavascriptInterface
    public void onDoubleTapJSResponse(String locationJson) {
        PointGeometry pg = CesiumUtils.getPointGeometryFromJson(locationJson);
        mOnMapGestureListener.onDoubleTap(pg);
    }

    @JavascriptInterface
    public void updateViewedLocation(String locationJson) {
        PointGeometry pg = CesiumUtils.getPointGeometryFromJson(locationJson);
        mViewedLocationHolder.setData(pg);
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
                mViewWidth = v.getWidth();
                mViewHeight = v.getHeight();
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

}
