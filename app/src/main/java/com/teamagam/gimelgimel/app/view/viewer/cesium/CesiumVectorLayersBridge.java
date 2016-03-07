package com.teamagam.gimelgimel.app.view.viewer.cesium;

import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.MultipleLocationsEntity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polygon;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polyline;

/**
 * Created by Bar on 01-Mar-16.
 * <p/>
 * A class for communication via pure javascript with
 * the Cesium viewer
 */
public class CesiumVectorLayersBridge {

    private JavascriptCommandExecutor mJsExecutor;

    private static final String JS_VAR_PREFIX_LAYER = "gglayer_";


    public CesiumVectorLayersBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        this.mJsExecutor = javascriptCommandExecutor;
    }

    public void addLayer(VectorLayer vectorLayer) {
        addLayer(vectorLayer.getId());
        for (Entity entity : vectorLayer.getEntities()) {
            addEntity(vectorLayer.getId(), entity);
        }
    }

    public void removeLayer(VectorLayer vectorLayer) {
        String layerJsName = getLayerJsVarName(vectorLayer.getId());
        String jsLine = String.format("GG.layerManager.addLayer(%s)", layerJsName);

        mJsExecutor.executeJsCommand(jsLine);
    }

    //TODO: change this with a visitor that adds entity for each type
    public void addEntity(String layerId, Entity entity) {

        String layerJsName = getLayerJsVarName(layerId);
        CesiumEntitiesAdder adder = new CesiumEntitiesAdder(layerJsName, mJsExecutor);
        entity.accept(adder);

//        Class<? extends Entity> runtimeEntityType = entity.getClass();
//
//        if (runtimeEntityType == Point.class) {
//            addPoint(layerId, (Point) entity);
//        } else if (runtimeEntityType == Polyline.class) {
//            addPolyline(layerId, (Polyline) entity);
//        } else if (runtimeEntityType == Polygon.class) {
//            addPolygon(layerId, (Polygon) entity);
//        } else {
//            throw new UnsupportedOperationException(
//                    "No support for given entity: " + runtimeEntityType.getSimpleName());
//        }
    }


    public void updateEntity(String layerId, Entity entity) {

        String layerJsVarName = getLayerJsVarName(layerId);

        CesiumEntitiesUpdater updater = new CesiumEntitiesUpdater(layerJsVarName, mJsExecutor);
        entity.accept(updater);
//        Class<? extends Entity> runtimeEntityType = entity.getClass();
//
//        if (runtimeEntityType == Point.class) {
//            updatePoint(layerId, (Point) entity);
//        } else if (runtimeEntityType == Polyline.class) {
//            updatePolyline(layerId, (Polyline) entity);
//        } else if (runtimeEntityType == Polygon.class) {
//            updatePolygon(layerId, (Polygon) entity);
//        } else {
//            throw new UnsupportedOperationException(
//                    "No support for given entity: " + runtimeEntityType.getSimpleName());
//        }
    }

    public void removeEntity(String layerId, Entity entity) {
        String layerJsName = getLayerJsVarName(layerId);
        String jsLine = String.format("%s.removeEntity(\"%s\");", layerJsName, entity.getId());

        //Add marker to given layer
        mJsExecutor.executeJsCommand(jsLine);
    }

    private void addLayer(String layerId) {
        String layerJsName = getLayerJsVarName(layerId);

        //define new layer
        String layerCreation = String.format("var %s = new GG.Layers.VectorLayer(\"%s\");",
                layerJsName, layerId);
        mJsExecutor.executeJsCommand(layerCreation);

        //Add new layer to layerManager
        String layerAddition = String.format("GG.layerManager.addLayer(%s);", layerJsName);
        mJsExecutor.executeJsCommand(layerAddition);
    }

    //Utils
    private static String getLayerJsVarName(String layerId) {
        return String.format("%s%s", JS_VAR_PREFIX_LAYER, layerId);
    }

    /***
     * An interface used to inject {@link CesiumVectorLayersBridge}
     * with Javascript execution capability
     */
    public interface JavascriptCommandExecutor {

        void executeJsCommand(String line);
    }
}
