package com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces;

import android.webkit.JavascriptInterface;

import com.teamagam.gimelgimel.app.view.viewer.cesium.CesiumUtils;
import com.teamagam.gimelgimel.app.view.viewer.cesium.SynchronizedDataHolder;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * A {@link android.webkit.JavascriptInterface} methods class
 * object with currently selected location update functionality
 */
public class SelectedLocationUpdater {

    public static final String JAVASCRIPT_INTERFACE_NAME = "LocationUpdater";

    private SynchronizedDataHolder<PointGeometry> mLocationHolder;

    public SelectedLocationUpdater(
            SynchronizedDataHolder<PointGeometry> synchronizedLocationHolder) {
        this.mLocationHolder = synchronizedLocationHolder;
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
        mLocationHolder.setData(pg);
    }
}
