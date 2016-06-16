package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.common.logging.LogWrapper;
import com.teamagam.gimelgimel.app.common.logging.LogWrapperFactory;

/**
 * Created by Yoni on 3/7/2016.
 */
public abstract class CesiumBaseBridge {

    protected JavascriptCommandExecutor mJsExecutor;
    protected final LogWrapper sLogger = LogWrapperFactory.create(this.getClass());

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
