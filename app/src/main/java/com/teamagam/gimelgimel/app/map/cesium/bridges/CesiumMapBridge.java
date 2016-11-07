package com.teamagam.gimelgimel.app.map.cesium.bridges;

import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.map.cesium.CesiumUtils;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;

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

     public void flyTo(PointGeometryApp point) {
        String flyToPoint = String.format("%s.flyTo(%s);", JS_VAR_PREFIX_CAMERA,
                CesiumUtils.getLocationJson(point));
        mJsExecutor.executeJsCommand(flyToPoint);
    }

    public void zoomTo(PointGeometryApp point) {
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

