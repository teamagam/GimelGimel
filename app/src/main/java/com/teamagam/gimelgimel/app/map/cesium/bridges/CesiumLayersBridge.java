package com.teamagam.gimelgimel.app.map.cesium.bridges;

import com.teamagam.gimelgimel.app.map.model.GGLayer;

/**
 * Created by Yoni on 3/8/2016.
 */
public abstract class CesiumLayersBridge<LayerType extends GGLayer> extends CesiumBaseBridge {

    private static final String JS_VAR_PREFIX_LAYER = "gglayer_";

    public CesiumLayersBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    public abstract void addLayer(LayerType layer);

    public void removeLayer(LayerType vectorLayer) {
        mJsExecutor.executeJsCommand(
                String.format("GG.layerManager.removeLayer('%s')", vectorLayer.getId()));
    }

    protected void defineJSLayer(String layerId) {
        String layerJsName = getLayerJsVarName(layerId);

        //define new layer
        String layerCreation = String.format("var %s = new GG.Layers.%s(\"%s\");",
                layerJsName, getCesiumLayerType(), layerId);
        mJsExecutor.executeJsCommand(layerCreation);
    }

    protected void addLayerToManager(String layerId) {
        //Add new layer to layerManager

        String layerAddition = String.format("GG.layerManager.addLayer(%s);",
                getLayerJsVarName(layerId));
        mJsExecutor.executeJsCommand(layerAddition);
    }

    protected abstract String getCesiumLayerType();

    //Utils
    protected static String getLayerJsVarName(String layerId) {
        return String.format("%s%s", JS_VAR_PREFIX_LAYER, layerId);
    }
}