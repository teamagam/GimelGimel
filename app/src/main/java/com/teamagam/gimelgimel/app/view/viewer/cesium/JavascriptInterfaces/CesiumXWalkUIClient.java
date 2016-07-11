package com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces;

import com.teamagam.gimelgimel.app.view.viewer.cesium.CesiumMapView;

import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

/**
 * A {@link XWalkUIClient} to control our {@link CesiumMapView}
 */
public class CesiumXWalkUIClient extends XWalkUIClient {

    private static final String CESIUM_LIBRARY_FILE = "cesium";
    private static final String CESIUM_ERROR_MESSAGE = "an error occurred while accessing";

    CesiumJsErrorListener mErrorListener;

    public CesiumXWalkUIClient(XWalkView view, CesiumJsErrorListener errorListener) {
        super(view);
        mErrorListener = errorListener;
    }

    @Override
    public boolean onConsoleMessage(XWalkView view, String message, int lineNumber, String sourceId, ConsoleMessageType messageType) {
        if (shouldRaiseEvent(sourceId, message)) {
            raiseEvent(message);
        }

        // Notify that the onConsoleMessage is overridden
        return true;
    }

    private boolean shouldRaiseEvent(String fileSource, String message) {
        // If the console message comes from Cesium, it's an error.
        // A listener should be called.
        if (fileSource.toLowerCase().contains(CESIUM_LIBRARY_FILE)) {
            if (message.toLowerCase().contains(CESIUM_ERROR_MESSAGE)) {
                return true;
            }
        }

        return false;
    }

    private void raiseEvent(String message) {
        if (mErrorListener != null) {
            mErrorListener.onCesiumError(message);
        }
    }

    /**
     * Listens to a Cesium javascript errors
     */
    public interface CesiumJsErrorListener {
        void onCesiumError(String error);
    }

}
