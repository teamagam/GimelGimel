package com.teamagam.gimelgimel.app.view.viewer.cesium;

import com.teamagam.gimelgimel.app.view.viewer.data.GGLayer;

/**
 * Created by Yoni on 3/8/2016.
 */
public abstract class CesiumLayersBridge extends CesiumBaseBridge {

    private static final String JS_VAR_PREFIX_LAYER = "gglayer_";

    public CesiumLayersBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    public void removeLayer(GGLayer vectorLayer) {
        mJsExecutor.executeJsCommand(String.format("GG.layerManager.removeLayer(%s)", vectorLayer.getId()));
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

        String layerAddition = String.format("GG.layerManager.addLayer(%s);", getLayerJsVarName(layerId));
        mJsExecutor.executeJsCommand(layerAddition);
    }

    protected abstract String getCesiumLayerType();

    public abstract void addLayer(GGLayer layer);

    //Utils
    protected static String getLayerJsVarName(String layerId) {
        return String.format("%s%s", JS_VAR_PREFIX_LAYER, layerId);
    }
}
