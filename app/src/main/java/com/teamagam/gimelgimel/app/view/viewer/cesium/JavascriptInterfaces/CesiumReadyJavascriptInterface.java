package com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces;

import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;

import org.xwalk.core.JavascriptInterface;

/**
 * A {@link org.xwalk.core.JavascriptInterface} method wrapper class
 * for "on-ready" cesium viewer event
 */
public class CesiumReadyJavascriptInterface {

    public static final String JAVASCRIPT_INTERFACE_NAME = "CesiumReady";

    private static final Logger sLogger = LoggerFactory.create(
            CesiumReadyJavascriptInterface.class);

    private CesiumReadyListener mCesiumReadyListener;

    public CesiumReadyJavascriptInterface(CesiumReadyListener listener) {
        mCesiumReadyListener = listener;
    }

    /**
     * Notifies system when Cesium viewer is ready, i.e. finished loading and set to work
     */
    @JavascriptInterface
    public void onReady() {
        sLogger.v("Cesium viewer is ready");
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
