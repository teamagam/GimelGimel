package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.webkit.ValueCallback;

/**
 * Created by Yoni on 3/7/2016.
 */
public abstract class CesiumBaseBridge {

    protected JavascriptCommandExecutor mJsExecutor;

    public CesiumBaseBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
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
