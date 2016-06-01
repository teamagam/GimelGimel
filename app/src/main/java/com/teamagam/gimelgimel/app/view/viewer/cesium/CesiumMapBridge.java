package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.webkit.ValueCallback;

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

    public void setExtent(Collection<Entity> extent) {
        //TODO: zoom should be to the extent or maybe layer would be better?
        //zoom to extent
        String zoomToExtent = JS_VAR_PREFIX_CAMERA + ".zoomTo(" + JS_VAR_PREFIX_CAMERA + ".entities);";
        mJsExecutor.executeJsCommand(zoomToExtent);
    }

    //TODO: consider the use of one js method with an object arguemnt of 3D/2D point.
    public void zoomTo(float longitude, float latitude, float altitude) {
        zoomTo(new PointGeometry(latitude, longitude, altitude));
    }

    public void zoomTo(float longitude, float latitude) {
        zoomTo(new PointGeometry(latitude, longitude));
    }

    public void zoomTo(PointGeometry point) {
        String zoomToPoint = String.format("%s.zoomTo(%s);", JS_VAR_PREFIX_CAMERA,
                CesiumUtils.getLocationJson(point));
        mJsExecutor.executeJsCommand(zoomToPoint);
    }

    public void getPosition(ValueCallback<String> callback) {
        String getPosition = String.format("%s.getCameraPosition();", JS_VAR_PREFIX_CAMERA);
        mJsExecutor.executeJsCommandForResult(getPosition, callback);
    }
}

