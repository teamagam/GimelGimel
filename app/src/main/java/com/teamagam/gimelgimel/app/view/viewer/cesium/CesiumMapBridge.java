package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;

import java.util.Collection;

/**
 * Created by Yoni on 3/7/2016.
 */
public class CesiumMapBridge extends CesiumBaseBridge {

    private static final String JS_VAR_PREFIX_VIEWER = "GG.viewer";
    private static final long TIME_OUT_SECONDS = 2;

    public CesiumMapBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    public void setExtent(float west, float south, float east, float north) {
        String zoomToRectangle = String.format(
                "%s.zoomToRectangle(%f, %f, %f, %f);",
                JS_VAR_PREFIX_VIEWER, west, south, east, north);
        mJsExecutor.executeJsCommand(zoomToRectangle);
    }

    public void setExtent(Collection<Entity> extent) {
        //TODO: zoom should be to the extent
        //zoom to extent
        String zoomToExtent = JS_VAR_PREFIX_VIEWER + ".zoomTo(" + JS_VAR_PREFIX_VIEWER + ".entities);";
        mJsExecutor.executeJsCommand(zoomToExtent);
    }

    //TODO: consider the use of one js method with an object arguemnt of 3D/2D point.
    public void zoomTo(float longitude, float latitude, float altitude) {
        String zoomToPoint = String.format("%s.zoomTo3Point(%f,%f,%f);",
                JS_VAR_PREFIX_VIEWER, longitude, latitude, altitude);
        mJsExecutor.executeJsCommand(zoomToPoint);
    }

    public void zoomTo(float longitude, float altitude) {
        String zoomToPoint = String.format("%s.zoomTo2Point(%f,%f);", JS_VAR_PREFIX_VIEWER, longitude, altitude);
        mJsExecutor.executeJsCommand(zoomToPoint);
    }


    public void getPosition(ValueCallback<String> callback) {
        String getPosition = String.format("%s.getCameraPosition();",JS_VAR_PREFIX_VIEWER);
        mJsExecutor.executeJsCommandForResult(getPosition, callback);
    }

}

