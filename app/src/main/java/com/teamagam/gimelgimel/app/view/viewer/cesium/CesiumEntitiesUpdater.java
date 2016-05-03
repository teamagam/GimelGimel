package com.teamagam.gimelgimel.app.view.viewer.cesium;

/**
 * Created by Bar on 06-Mar-16.
 */
public class CesiumEntitiesUpdater extends BaseCesiumEntitiesHandler {

    public CesiumEntitiesUpdater(String layerJsName,
                                 CesiumVectorLayersBridge.JavascriptCommandExecutor executor) {
        super("update", layerJsName, executor);
    }
}
