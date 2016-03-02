package com.teamagam.gimelgimel.app.view.viewer;

import com.teamagam.gimelgimel.app.view.viewer.data.Location;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polygon;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polyline;

/**
 * Created by Bar on 01-Mar-16.
 */
public class CesiumVectorLayersBridge {

    private JavascriptCommandExecutor mJsExecutor;

    private static final String JS_VAR_PREFIX_LAYER = "gglayer_";


    public CesiumVectorLayersBridge(JavascriptCommandExecutor javascriptCommandExecutor) {
        this.mJsExecutor = javascriptCommandExecutor;
    }

    public void initializeLayer(VectorLayer vectorLayer) {
        addLayer(vectorLayer.getId());
        for (Entity entity : vectorLayer.getEntities()) {
            addEntity(vectorLayer.getId(), entity);
        }
    }

    private String getLayerJsVarName(String layerId) {
        return String.format("%s%s", JS_VAR_PREFIX_LAYER, layerId);
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

    //TODO: change this with a visitor that adds entity for each type
    public void addEntity(String layerId, Entity entity) {
        Class<? extends Entity> runtimeEntityType = entity.getClass();

        if (runtimeEntityType == Point.class) {
            addPoint(layerId, (Point) entity);
        } else if (runtimeEntityType == Polyline.class) {
            addPolyline(layerId, (Polyline) entity);
        } else if (runtimeEntityType == Polygon.class) {
            addPolygon(layerId, (Polygon) entity);
        } else {
            throw new UnsupportedOperationException(
                    "No support for given entity: " + runtimeEntityType.getSimpleName());
        }
    }

    public void updateEntity(String layerId, Entity entity) {
        Class<? extends Entity> runtimeEntityType = entity.getClass();

        if (runtimeEntityType == Point.class) {
            updatePoint(layerId, (Point) entity);
        } else if (runtimeEntityType == Polyline.class) {
            updatePolyline(layerId, (Polyline) entity);
        } else if (runtimeEntityType == Polygon.class) {
            updatePolygon(layerId, (Polygon) entity);
        } else {
            throw new UnsupportedOperationException(
                    "No support for given entity: " + runtimeEntityType.getSimpleName());
        }
    }

    public void removeEntity(String layerId, Entity entity) {
        String layerJsName = getLayerJsVarName(layerId);
        String jsLine = String.format("%s.removeEntity(\"%s\");", layerJsName, entity.getId());

        //Add marker to given layer
        mJsExecutor.executeJsCommand(jsLine);
    }

    private void updatePolygon(String layerId, Polygon entity) {
    }

    private void updatePolyline(String layerId, Polyline entity) {
    }

    private void updatePoint(String layerId, Point pointEntity) {
        String layerJsName = getLayerJsVarName(layerId);
        String pointLocationJson = getLocationJson(pointEntity);
        String jsLine = String.format("%s.updateMarker(\"%s\",%s);", layerJsName,
                pointEntity.getId(),
                pointLocationJson);

        //Add marker to given layer
        mJsExecutor.executeJsCommand(jsLine);
    }

    private void addPolyline(String layerId, Polyline entity) {
    }

    private void addPolygon(String layerId, Polygon entity) {
    }




    private void addPoint(String layerId, Point pointEntity) {
        String layerJsName = getLayerJsVarName(layerId);
        String pointLocationJson = getLocationJson(pointEntity);
        String jsLine = String.format("%s.addMarker(\"%s\",%s);", layerJsName, pointEntity.getId(),
                pointLocationJson);

        //Add marker to given layer
        mJsExecutor.executeJsCommand(jsLine);
    }


    //Utils
    private static String getLocationJson(Point pointEntity) {
        Location pLoc = pointEntity.getLocation();
        return String.format("{latitude: %s, longitude: %s}", pLoc.latitude, pLoc.longitude);
    }

    public interface JavascriptCommandExecutor {

        void executeJsCommand(String line);
    }
}
