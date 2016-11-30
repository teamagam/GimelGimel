package com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces;

import com.teamagam.gimelgimel.app.common.utils.Constants;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

/**
 * A {@link XWalkResourceClient} to control our {@link XWalkView}
 */
public class CesiumXWalkResourceClient extends XWalkResourceClient {

    private CesiumReadyListener mCesiumReadyListener;

    public CesiumXWalkResourceClient(XWalkView view, CesiumReadyListener listener) {
        super(view);
        mCesiumReadyListener = listener;
    }

    @Override
    public boolean shouldOverrideUrlLoading(XWalkView view, String url) {
        if (isGGCesiumFile(url)) {
            return false;
        }
        return true;
    }

    /**
     * Notifies system when Cesium viewer is ready, i.e. finished loading and set to work
     */
    @Override
    public void onLoadFinished(XWalkView view, String url) {
        if (isGGCesiumFile(url)) {
            mCesiumReadyListener.onCesiumReady();
        }
    }

    private boolean isGGCesiumFile(String url) {
        return url.equals(Constants.CESIUM_HTML_LOCAL_FILEPATH);
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
