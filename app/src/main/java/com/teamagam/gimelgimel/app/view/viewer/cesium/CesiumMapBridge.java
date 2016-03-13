package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.util.Log;
import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

;

/**
 * Created by Yoni on 3/7/2016.
 */
public class CesiumMapBridge extends CesiumBaseBridge{

    private static final String JS_VAR_PREFIX_VIEWER = "GG.viewer";
    private static final long TIME_OUT_SECONDS = 15;

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
        final CountDownLatch latch = new CountDownLatch(1);
        final MutableObject<String> result = new MutableObject<>();

        Log.d("Cesium", getPosition);
        mJsExecutor.executeJsCommandForResult(getPosition,
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("Cesium", "Callback!!!!!!!!");
                        Log.d("Cesium", value);
                        result.setValue(value);
                        latch.countDown();

                    }
                }
        );
        try {
            latch.await(TIME_OUT_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String value = result.getValue();
        if (value == null)
            Log.i("Cesium Bridge", "no value returned");
        else
            if (value.equals(""))
                Log.i("Cesium Bridge", "empty returned");
        Log.i("Cesium Bridge", value);
        PointGeometry point = CesiumUtils.getPointFromJson(value);
        return point; //may be null.
        }

}
