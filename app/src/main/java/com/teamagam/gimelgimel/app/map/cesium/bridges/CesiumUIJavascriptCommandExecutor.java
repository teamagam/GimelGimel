package com.teamagam.gimelgimel.app.map.cesium.bridges;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.cesium.CesiumMapView;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;

/***
 * Javascript execution capability which runs on the UI thread.
 * implements {@link CesiumBaseBridge.JavascriptCommandExecutor}
 */
public class CesiumUIJavascriptCommandExecutor implements CesiumBaseBridge.JavascriptCommandExecutor {

    protected final AppLogger sLogger = AppLoggerFactory.create(this.getClass());
    private CesiumMapView mCesiumMapView;

    public CesiumUIJavascriptCommandExecutor(CesiumMapView cesiumMapView) {
        mCesiumMapView = cesiumMapView;
    }

    @Override
    public void executeJsCommand(final String line) {
        mCesiumMapView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mCesiumMapView.load(String.format("javascript:%s", line), null);
                                }
                            }
        );

    }

    @Override
    public void executeJsCommandForResult(final String line,
                                          final ValueCallback<String> callback) {
        sLogger.d("JS for result: " + line);
        mCesiumMapView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mCesiumMapView.evaluateJavascript(line, callback);
                                }
                            }
        );
    }
}
