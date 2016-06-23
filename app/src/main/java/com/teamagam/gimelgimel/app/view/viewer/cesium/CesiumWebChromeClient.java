package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

/**
 * A {@link WebChromeClient} of Cesium.
 * Handles incoming events from the WebView containing Cesium.
 */
public class CesiumWebChromeClient extends WebChromeClient {

    private static final String CESIUM_LIBRARY_FILE = "cesium";
    private static final String CESIUM_ERROR_MESSAGE = "an error occurred while accessing";

    CesiumJsErrorListener mErrorListener;

    public CesiumWebChromeClient(CesiumJsErrorListener errorListener) {
        mErrorListener = errorListener;
    }



    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        String fileSource = consoleMessage.sourceId();
        String message = consoleMessage.message();

        if (shouldRaiseEvent(fileSource, message)) {
            RaiseEvent(message);
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

    private void RaiseEvent(String message) {
        if (mErrorListener != null) {
            mErrorListener.onError(message);
        }
    }

    /**
     * Listens to a Cesium javascript errors
     */
    public interface CesiumJsErrorListener {
        void onError(String error);
    }
}
