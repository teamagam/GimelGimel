package com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces;

import android.webkit.JavascriptInterface;

import com.teamagam.gimelgimel.app.common.SynchronizedDataHolder;
import com.teamagam.gimelgimel.app.view.viewer.cesium.CesiumUtils;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * A {@link android.webkit.JavascriptInterface} methods class
 * object with currently selected location update functionality
 */
public class LocationUpdater {

    public static final String JAVASCRIPT_INTERFACE_NAME = "LocationUpdater";

    private SynchronizedDataHolder<PointGeometry> mSelectedLocationHolder;
    private SynchronizedDataHolder<PointGeometry> mViewedLocationHolder;

    public LocationUpdater() {
        this.mSelectedLocationHolder = new SynchronizedDataHolder<>(PointGeometry.DEFAULT_POINT);
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
    public void UpdateSelectedLocation(String locationJson) {
        PointGeometry pg = CesiumUtils.getPointGeometryFromJson(locationJson);
        mSelectedLocationHolder.setData(pg);
    }

    @JavascriptInterface
    public void UpdateViewedLocation(String locationJson) {
        PointGeometry pg = CesiumUtils.getPointGeometryFromJson(locationJson);
        mViewedLocationHolder.setData(pg);
    }

    public PointGeometry getLastSelectedLocation() {
        return mSelectedLocationHolder.getData();
    }

    public void setLastSelectedLocation(PointGeometry pointGeometry) {
        mSelectedLocationHolder.setData(pointGeometry);
    }

    public PointGeometry getLastViewedLocation() {
        return mViewedLocationHolder.getData();
    }

    public void setLastViewedLocation(PointGeometry pointGeometry) {
        mSelectedLocationHolder.setData(pointGeometry);
    }
}
