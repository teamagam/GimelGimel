package com.teamagam.gimelgimel.app.view.viewer.cesium.bridges;

/**
 * todo: complete javadoc
 */
public class CesiumGestureBridge extends CesiumBaseBridge{

    private static final String JS_VAR_PREFIX_EVENT_HANDLER = "GG.eventHandler";

    public CesiumGestureBridge(CesiumBaseBridge.JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    public void onDoubleTap() {
        executeJSTouchEvent("onDoubleTap");
    }

    public void onLongPress() {
        executeJSTouchEvent("onLongPress");
    }

    public void onSingleTap() {
        executeJSTouchEvent("onSingleTap");
    }

    private void executeJSTouchEvent(String eventName){
        String doubleTapExec = String.format("%s.%s();", JS_VAR_PREFIX_EVENT_HANDLER,
                eventName);
        mJsExecutor.executeJsCommand(doubleTapExec);
    }
}
