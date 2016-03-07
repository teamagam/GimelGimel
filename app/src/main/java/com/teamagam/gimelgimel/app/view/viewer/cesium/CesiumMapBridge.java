package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.util.Log;

import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;

import java.util.Collection;

/**
 * Created by Yoni on 3/7/2016.
 */
public class CesiumMapBridge extends CesiumBaseBridge{

    private static final String JS_VAR_PREFIX_VIEWER = "GG.viewer";

    public CesiumMapBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    public void setExtent(float west, float south, float east, float north) {
        String rectangle = String.format("Cesium.Rectangle.fromDegrees(%f, %f, %f, %f)",
                west, south, east, north);
        String zoomToRectangle = JS_VAR_PREFIX_VIEWER + ".camera.flyTo({\n" +
                "    destination : " + rectangle + "\n});";
        mJsExecutor.executeJsCommand(zoomToRectangle);
        Log.i("Cesium", zoomToRectangle);
//        String zoomToRectangle = "Cesium.Rectangle.fromDegrees(117.940573, -29.808406, 118.313421, -29.468825);"
    }

    public void setExtent(Collection<Entity> extent) {
        //TODO: zoom should be to the extent
        //zoom to extent
        String zoomToExtent = JS_VAR_PREFIX_VIEWER + ".zoomTo(" + JS_VAR_PREFIX_VIEWER + ".entities);";
        mJsExecutor.executeJsCommand(zoomToExtent);
    }
}
