package com.teamagam.gimelgimel.app.view.viewer.cesium;

import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;

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
        mJsExecutor = javascriptCommandExecutor;
    }

    public void addLayer(VectorLayer vectorLayer) {
        addLayer(vectorLayer.getId());
        for (Entity entity : vectorLayer.getEntities()) {
            addEntity(vectorLayer.getId(), entity);
        }
    }

    public void removeLayer(VectorLayer vectorLayer) {
        String layerJsName = getLayerJsVarName(vectorLayer.getId());
        String jsLine = String.format("GG.layerManager.removeLayer(%s)", layerJsName);

        mJsExecutor.executeJsCommand(jsLine);
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
