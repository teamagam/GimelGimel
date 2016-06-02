package com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * A {@link android.webkit.JavascriptInterface} method wrapper class
 * for "on-ready" cesium viewer event
 */
public class CesiumReadyJavascriptInterface {

    public static final String JAVASCRIPT_INTERFACE_NAME = "CesiumReady";

    private static final String LOG_TAG = CesiumReadyJavascriptInterface.class.getSimpleName();

    private CesiumReadyListener mCesiumReadyListener;

    public CesiumReadyJavascriptInterface(CesiumReadyListener listener) {
        mCesiumReadyListener = listener;
    }

    /**
     * Notifies system when Cesium viewer is ready, i.e. finished loading and set to work
     */
    @JavascriptInterface
    public void onReady() {
        Log.v(LOG_TAG, "Cesium viewer is ready");
        mCesiumReadyListener.onCesiumReady();
    }

    /**
     * Listener interface for handling onCesiumReady events
     */
    public interface CesiumReadyListener {
        /**
         * Fired when cesium viewer is ready
         */
        void onCesiumReady();
    }
}
