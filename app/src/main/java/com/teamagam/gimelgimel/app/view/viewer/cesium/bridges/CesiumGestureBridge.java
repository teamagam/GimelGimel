package com.teamagam.gimelgimel.app.view.viewer.cesium.bridges;

/**
 * todo: complete javadoc
 */
public class CesiumGestureBridge extends CesiumBaseBridge{

    private static final String JS_VAR_PREFIX_EVENT_HANDLER = "GG.eventHandler";

    public CesiumGestureBridge(CesiumBaseBridge.JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    public void onDoubleTap(float relativeXPosition, float relativeYPosition) {
        executeJSTouchEvent("onDoubleTap", relativeXPosition, relativeYPosition);
    }

    public void onLongPress(float relativeXPosition, float relativeYPosition) {
        executeJSTouchEvent("onLongPress", relativeXPosition, relativeYPosition);
    }

    public void onSingleTap(float relativeXPosition, float relativeYPosition) {
        executeJSTouchEvent("onSingleTap", relativeXPosition, relativeYPosition);
    }

    private void executeJSTouchEvent(String eventName, float relativeXPosition,
                                     float relativeYPosition){
        String doubleTapExec = String.format("%s.%s(%s, %s);", JS_VAR_PREFIX_EVENT_HANDLER,
                eventName, relativeXPosition, relativeYPosition);
        mJsExecutor.executeJsCommand(doubleTapExec);
    }
}
