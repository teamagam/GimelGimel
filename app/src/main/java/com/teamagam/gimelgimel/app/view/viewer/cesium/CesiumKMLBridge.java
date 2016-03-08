package com.teamagam.gimelgimel.app.view.viewer.cesium;

import com.teamagam.gimelgimel.app.view.viewer.data.GGLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.KMLLayer;

/**
 * Created by Yoni on 3/8/2016.
 */
public class CesiumKMLBridge extends CesiumLayersBridge {

    public static final String KML_LAYER = "KMLLayer";

    public CesiumKMLBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        super(javascriptCommandExecutor);
    }

    @Override
    protected String getCesiumLayerType() {
        return KML_LAYER;
    }

    @Override
    public void addLayer(GGLayer layer) {
        defineJSLayer(layer.getId());

        String loadKML = String.format("%s.loadKML(\"%s\")", getLayerJsVarName(layer.getId()), ((KMLLayer) layer).getPath());
        mJsExecutor.executeJsCommand(loadKML);

        addLayerToManager(layer.getId());
    }

}
