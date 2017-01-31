package com.teamagam.gimelgimel.app.map.cesium.bridges;

/**
 * Created by Yoni on 3/8/2016.
 */
public class CesiumKMLBridge extends CesiumLayersBridge {

    public static final String KML_LAYER = "KMLLayer";

    public CesiumKMLBridge(CesiumBaseBridge.JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    @Override
    protected String getCesiumLayerType() {
        return KML_LAYER;
    }

    public void loadKml(String layerId, String path) {
        String layerJsVar = getLayerJsVarName(layerId);
        String jsCommand = String.format("%s.loadKML(\"file://%s\")", layerJsVar, path);

        mJsExecutor.executeJsCommand(jsCommand);
    }
}
