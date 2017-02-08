package com.teamagam.gimelgimel.app.map.esri;

import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.teamagam.gimelgimel.app.common.utils.BiMap;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public class GGDynamicLayer {

    private final GraphicsLayer mGraphicsLayer;
    private final BiMap<String, Integer> mEntityIdToGraphicId;
    private final SpatialReference mDataSR;
    private final SpatialReference mMapSR;

    public GGDynamicLayer(SpatialReference sourceSR, SpatialReference mapSR) {
        mDataSR = sourceSR;
        mMapSR = mapSR;

        mGraphicsLayer = new GraphicsLayer();
        mEntityIdToGraphicId = new BiMap<>();
    }


    public GraphicsLayer getGraphicsLayer() {
        return mGraphicsLayer;
    }

    public void draw(GeoEntity entity) {
        Graphic graphic = createGraphic(entity);
        int graphicId = mGraphicsLayer.addGraphic(graphic);
        mEntityIdToGraphicId.put(entity.getId(), graphicId);
    }

    public void remove(String entityId) {
        int gId = mEntityIdToGraphicId.getValue(entityId);
        mGraphicsLayer.removeGraphic(gId);
    }

    public void update(GeoEntity entity) {
        remove(entity.getId());
        draw(entity);
    }

    private Graphic createGraphic(GeoEntity entity) {
        Geometry geometry = transformToEsri((PointGeometryApp) entity.getGeometry());
        Symbol symbol = createSymbol(entity.getSymbol());
        return new Graphic(geometry, symbol);
    }

    private Symbol createSymbol(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol symbol) {
        return new TextSymbol(10, "Pin", Color.RED);
    }

    private Point transformToEsri(PointGeometryApp point) {
        Point p;
        if (point.hasAltitude) {
            p = new Point(point.longitude, point.latitude, point.altitude);
        } else {
            p = new Point(point.longitude, point.latitude);
        }

        return (Point) projectFromWGS84(p);
    }

    private Geometry projectFromWGS84(Geometry geometry) {
        return GeometryEngine.project(geometry, mDataSR, mMapSR);
    }
}
