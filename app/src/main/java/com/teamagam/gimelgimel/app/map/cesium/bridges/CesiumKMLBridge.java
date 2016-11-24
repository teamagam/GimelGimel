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

    @Override
    public void addLayer(String layerId) {
        defineJSLayer(layerId);

        String path = null;
        String loadKML = String.format("%s.loadKML(\"%s\")", getLayerJsVarName(layerId), path);
        mJsExecutor.executeJsCommand(loadKML);

        addLayerToManager(layerId);
    }

}
