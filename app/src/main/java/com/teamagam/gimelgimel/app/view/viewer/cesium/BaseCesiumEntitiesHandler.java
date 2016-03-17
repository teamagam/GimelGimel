package com.teamagam.gimelgimel.app.view.viewer.cesium;

import com.teamagam.gimelgimel.app.view.viewer.IEntitiesVisitor;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polygon;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polyline;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

/**
 * Created by Bar on 07-Mar-16.
 */
public class BaseCesiumEntitiesHandler implements IEntitiesVisitor {

    private String mMethodPrefix;
    private String mLayerJsName;
    private CesiumVectorLayersBridge.JavascriptCommandExecutor mJsExecutor;

    public BaseCesiumEntitiesHandler(String methodPrefix, String layerJsName,
                                     CesiumVectorLayersBridge.JavascriptCommandExecutor executor) {
        mMethodPrefix = methodPrefix;
        mLayerJsName = layerJsName;
        mJsExecutor = executor;
    }

    @Override
    public void visit(Point point) {
        String pointLocationJson = CesiumUtils.getLocationJson((PointGeometry) point.getGeometry());
        String symbolJson = CesiumUtils.getBillboardJson(point);

        executeMethodOnLayer(point, pointLocationJson, symbolJson, "Marker");
    }

    @Override
    public void visit(Polyline polyline) {
        String locationsJson = CesiumUtils.getLocationsJson(polyline);
        String polylineSymbolJson = CesiumUtils.getPolylineSymbolJson(polyline);

        executeMethodOnLayer(polyline, locationsJson, polylineSymbolJson, "Polyline");
    }

    @Override
    public void visit(Polygon polygon) {
        String locationsJson = CesiumUtils.getLocationsJson(polygon);
        String polygonSymbolJson = CesiumUtils.getPolygonSymbolJson(polygon);

        executeMethodOnLayer(polygon, locationsJson, polygonSymbolJson, "Polygon");
    }

    /***
     * Used to avoid code duplication calling js method
     *
     * @param entity       - entity to manipulate
     * @param geometryJson - wanted geometry (following the Cesium-Android API)
     * @param symbolJson   - wanted symbol (following the Cesium-Android API)
     * @param methodSuffix - one of the suffixes available for entity manipulation
     *                     (Marker/Polyline/Polygon)
     */
    private void executeMethodOnLayer(Entity entity, String geometryJson, String symbolJson,
                                      String methodSuffix) {
        String jsLine = String.format("%s.%s%s(\"%s\",%s, %s);",
                mLayerJsName, mMethodPrefix, methodSuffix, entity.getId(), geometryJson,
                symbolJson);

        mJsExecutor.executeJsCommand(jsLine);
    }
}
