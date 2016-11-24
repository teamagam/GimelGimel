package com.teamagam.gimelgimel.app.map.cesium.bridges;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

/**
 * base JS-Java bridge used for Cesium viewer.
 */
public abstract class CesiumBaseBridge {

    protected final Logger sLogger = LoggerFactory.create(this.getClass());
    protected JavascriptCommandExecutor mJsExecutor;

    public CesiumBaseBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        sLogger.d("starting JS Bridge");
        mJsExecutor = javascriptCommandExecutor;
    }

    /***
     * An interface used to inject {@link CesiumVectorLayersBridge}
     * with Javascript execution capability
     */
    public interface JavascriptCommandExecutor {

        void executeJsCommand(String line);

        void executeJsCommandForResult(String line, ValueCallback<String> callback);
    }
}
