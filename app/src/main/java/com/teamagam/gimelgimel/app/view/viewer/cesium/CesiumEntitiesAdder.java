package com.teamagam.gimelgimel.app.view.viewer.cesium;

/**
 * Created by Bar on 06-Mar-16.
 */
public class CesiumEntitiesAdder extends BaseCesiumEntitiesHandler {

    public CesiumEntitiesAdder(String layerJsName,
                               CesiumVectorLayersBridge.JavascriptCommandExecutor executor) {
        super("add", layerJsName, executor);
    }
}
