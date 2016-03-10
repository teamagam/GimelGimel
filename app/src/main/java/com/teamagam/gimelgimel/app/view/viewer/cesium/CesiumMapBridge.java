package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.util.Log;
import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

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
        String rectangle = String.format(".zoomToRectangle(%f, %f, %f, %f);", west, south, east, north);
        String zoomToRectangle = JS_VAR_PREFIX_VIEWER + rectangle;
        Log.d("Cesium", zoomToRectangle);
        mJsExecutor.executeJsCommand(zoomToRectangle);
//        Log.i("Cesium", zoomToRectangle);
//        String zoomToRectangle = "Cesium.Rectangle.fromDegrees(117.940573, -29.808406, 118.313421, -29.468825);"
    }

    public void setExtent(Collection<Entity> extent) {
        //TODO: zoom should be to the extent
        //zoom to extent
        String zoomToExtent = JS_VAR_PREFIX_VIEWER + ".zoomTo(" + JS_VAR_PREFIX_VIEWER + ".entities);";
        mJsExecutor.executeJsCommand(zoomToExtent);
    }

    public void zoomTo(float x, float y, float z) {
        String point = String.format(".zoomTo3Point(%f,%f,%f);",x,y,z);
        String zoomToPoint = JS_VAR_PREFIX_VIEWER + point;
        Log.d("Cesium", zoomToPoint);
        mJsExecutor.executeJsCommand(zoomToPoint);


    }

    public void zoomTo(float x, float y) {
        String point = String.format(".zoomTo2Point(%f,%f);",x,y);
        String zoomToPoint = JS_VAR_PREFIX_VIEWER + point;
        Log.d("Cesium", zoomToPoint);
        mJsExecutor.executeJsCommand(zoomToPoint);


    }


    public PointGeometry getPosition() {
        String getPosition = JS_VAR_PREFIX_VIEWER + ".getPosition();";
        Log.d("Cesium", getPosition);
        mJsExecutor.executeJsCommandForResult(getPosition,
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        
                        Log.d("Cesium", value);
                    }
                }
        );
        return null;
    }
}
