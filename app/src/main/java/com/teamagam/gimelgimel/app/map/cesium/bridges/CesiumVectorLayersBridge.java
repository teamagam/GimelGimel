package com.teamagam.gimelgimel.app.map.cesium.bridges;

import com.teamagam.gimelgimel.app.map.cesium.CesiumEntitiesAdder;
import com.teamagam.gimelgimel.app.map.cesium.CesiumEntitiesUpdater;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;

/**
 * Created by Bar on 01-Mar-16.
 * <p/>
 * A class for communication via pure javascript with
 * the Cesium viewer
 */
public class CesiumVectorLayersBridge extends CesiumLayersBridge {

    private static final String VECTOR_LAYER = "VectorLayer";

    public CesiumVectorLayersBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    @Override
    protected String getCesiumLayerType() {
        return VECTOR_LAYER;
    }

    //TODO: change this with a visitor that adds entity for each type
    public void addEntity(String layerId, Entity entity) {
        String layerJsName = getLayerJsVarName(layerId);

        CesiumEntitiesAdder adder = new CesiumEntitiesAdder(layerJsName, mJsExecutor);
        entity.accept(adder);
    }


    public void updateEntity(String layerId, Entity entity) {
        String layerJsVarName = getLayerJsVarName(layerId);

        CesiumEntitiesUpdater updater = new CesiumEntitiesUpdater(layerJsVarName, mJsExecutor);
        entity.accept(updater);
    }

    public void removeEntity(String layerId, Entity entity) {
        String layerJsName = getLayerJsVarName(layerId);
        String jsLine = String.format("%s.removeEntity(\"%s\");", layerJsName, entity.getId());

        //Add marker to given layer
        mJsExecutor.executeJsCommand(jsLine);
    }

    /**
     * calls defineJSLayer from abstract class {@link CesiumLayersBridge}
     *
     * @param layerId
     */
    @Override
    public void addLayer(String layerId) {
        defineJSLayer(layerId);
        addLayerToManager(layerId);
    }
}
