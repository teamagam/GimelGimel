package com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces;


import com.teamagam.gimelgimel.app.common.SynchronizedDataHolder;
import com.teamagam.gimelgimel.app.view.viewer.cesium.CesiumUtils;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.gestures.OnMapGestureListener;

import org.xwalk.core.JavascriptInterface;

/**
 * A {@link org.xwalk.core.JavascriptInterface} methods class
 * object with currently selected location update functionality
 */
public class CesiumMapGestureDetector {

    public static final String JAVASCRIPT_INTERFACE_NAME = "CesiumMapGestureDetector";

    private SynchronizedDataHolder<PointGeometry> mViewedLocationHolder;
    private OnMapGestureListener mOnMapGestureListener;

    public CesiumMapGestureDetector() {
        this.mViewedLocationHolder = new SynchronizedDataHolder<>(PointGeometry.DEFAULT_POINT);
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

    public PointGeometry getLastViewedLocation() {
        return mViewedLocationHolder.getData();
    }

    public void setOnMapGestureListener(OnMapGestureListener onMapGestureListener) {
        mOnMapGestureListener = onMapGestureListener;
    }
}
