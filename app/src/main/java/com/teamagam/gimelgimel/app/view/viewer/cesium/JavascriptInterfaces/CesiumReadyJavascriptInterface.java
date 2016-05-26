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

    private CesiumReadyListener mListener;

    public CesiumReadyJavascriptInterface(CesiumReadyListener listener) {
        mListener = listener;
    }

    /**
     * Notifies system when Cesium viewer is ready, i.e. finished loading and set to work
     */
    @JavascriptInterface
    public void onReady() {
        Log.v(LOG_TAG, "Cesium viewer is ready");
        mListener.onReady();
    }

    /**
     * Listener interface for handling onReady events
     */
    public interface CesiumReadyListener {
        /**
         * Fired when cesium viewer is ready
         */
        void onReady();
    }
}
