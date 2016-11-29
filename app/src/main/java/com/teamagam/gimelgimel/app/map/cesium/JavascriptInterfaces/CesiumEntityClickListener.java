package com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces;


import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;

import org.xwalk.core.JavascriptInterface;

/**
 * A {@link JavascriptInterface} methods class
 * object with entity clicked functionality
 */
public class CesiumEntityClickListener {

    public static final String JAVASCRIPT_INTERFACE_NAME = "CesiumEntityClickListener";
    private static final AppLogger sLogger = AppLoggerFactory.create(
            CesiumEntityClickListener.class);
    private final OnEntityClickListener mOnEntityClickListener;

    public CesiumEntityClickListener(OnEntityClickListener listener) {
        mOnEntityClickListener = listener;
    }

    /**
     * A method exposed for javascript execution.
     * Updates selected entity was clicked
     *
     * @param layerId  - id of the layer that have been clicked on
     * @param entityId - id of the entity that have been clicked on
     */
    @JavascriptInterface
    public void OnEntityClicked(String layerId, String entityId) {
        sLogger.userInteraction("Cesium entity of layer id " + layerId + "and entity id " +
                entityId + "was clicked.");
        mOnEntityClickListener.onCesiumEntityClick(layerId, entityId);
    }

    /**
     * Listener interface for handling Cesium Entities click events
     */
    public interface OnEntityClickListener {
        void onCesiumEntityClick(String layer, String entity);
    }

}
