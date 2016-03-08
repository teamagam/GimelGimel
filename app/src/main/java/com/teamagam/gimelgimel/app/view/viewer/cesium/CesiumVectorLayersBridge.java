package com.teamagam.gimelgimel.app.view.viewer.cesium;

import com.teamagam.gimelgimel.app.view.viewer.data.GGLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;

/**
 * Created by Bar on 01-Mar-16.
 * <p/>
 * A class for communication via pure javascript with
 * the Cesium viewer
 */
public class CesiumVectorLayersBridge extends CesiumLayersBridge{

    public static final String VECTOR_LAYER = "VectorLayer";

    public CesiumVectorLayersBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    @Override
    protected String getCesiumLayerType() {
        return VECTOR_LAYER;
    }

    @Override
    public void addLayer(GGLayer layer) {
        defineJSLayer(layer.getId());
        addLayerToManager(layer.getId());
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

    /**
     * calls defineJSLayer from abstract class {@link CesiumLayersBridge}
     * @param vectorLayer
     */
    public void addLayer(VectorLayer vectorLayer) {
        defineJSLayer(vectorLayer.getId());
        for (Entity entity : vectorLayer.getEntities()) {
            addEntity(vectorLayer.getId(), entity);
        }
        addLayerToManager(vectorLayer.getId());
    }

}
