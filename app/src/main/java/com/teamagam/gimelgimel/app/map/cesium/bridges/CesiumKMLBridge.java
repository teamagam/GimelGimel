package com.teamagam.gimelgimel.app.map.cesium.bridges;

import com.teamagam.gimelgimel.app.map.model.KMLLayer;

/**
 * Created by Yoni on 3/8/2016.
 */
public class CesiumKMLBridge extends CesiumLayersBridge<KMLLayer> {

    public static final String KML_LAYER = "KMLLayer";

    public CesiumKMLBridge(CesiumBaseBridge.JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    @Override
    protected String getCesiumLayerType() {
        return KML_LAYER;
    }

    @Override
    public void addLayer(KMLLayer layer) {
        defineJSLayer(layer.getId());

        String loadKML = String.format("%s.loadKML(\"%s\")", getLayerJsVarName(layer.getId()),  layer.getPath());
        mJsExecutor.executeJsCommand(loadKML);

        addLayerToManager(layer.getId());
    }

}
