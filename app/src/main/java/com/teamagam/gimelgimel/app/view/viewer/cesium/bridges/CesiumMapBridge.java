package com.teamagam.gimelgimel.app.view.viewer.cesium.bridges;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.view.viewer.cesium.CesiumUtils;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Collection;

/**
 * Created by Yoni on 3/7/2016.
 */
public class CesiumMapBridge extends CesiumBaseBridge {

    private static final String JS_VAR_PREFIX_CAMERA = "GG.cameraManager";

    public CesiumMapBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    public void setExtent(float west, float south, float east, float north) {
        String zoomToRectangle = String.format(
                "%s.zoomToRectangle(%f, %f, %f, %f);",
                JS_VAR_PREFIX_CAMERA, west, south, east, north);
        mJsExecutor.executeJsCommand(zoomToRectangle);
    }

     public void flyTo(PointGeometry point) {
        String zoomToPoint = String.format("%s.zoomTo(%s);", JS_VAR_PREFIX_CAMERA,
                CesiumUtils.getLocationJson(point));
        mJsExecutor.executeJsCommand(zoomToPoint);
    }

    public void getPosition(ValueCallback<String> callback) {
        String getPosition = String.format("%s.getCameraPosition();", JS_VAR_PREFIX_CAMERA);
        mJsExecutor.executeJsCommandForResult(getPosition, callback);
    }

    public void reloadImageryProvider() {
        String reloadImageryProvider = "GG.layerManager.reloadImageryProvider();";
        mJsExecutor.executeJsCommand(reloadImageryProvider);
    }
}

