package com.teamagam.gimelgimel.app.view.viewer.cesium.bridges;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.view.viewer.cesium.CesiumMapView;

/***
 * Javascript execution capability which runs on the UI thread.
 * implements {@link CesiumBaseBridge.JavascriptCommandExecutor}
 */
public class CesiumUIJavascriptCommandExecutor implements CesiumBaseBridge.JavascriptCommandExecutor {

    private CesiumMapView mCesiumMapView;
    protected final Logger sLogger = LoggerFactory.create(this.getClass());

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
