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
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public class GraphicsLayerGGAdapter {

    private final GraphicsLayer mGraphicsLayer;
    private final SpatialReference mDataSR;
    private final SpatialReference mMapSR;
    private final BiMap<String, Integer> mEntityIdToGraphicId;

    public GraphicsLayerGGAdapter(GraphicsLayer graphicsLayer, SpatialReference sourceSR,
                                  SpatialReference mapSR) {
        mGraphicsLayer = graphicsLayer;
        mDataSR = sourceSR;
        mMapSR = mapSR;
        mEntityIdToGraphicId = new BiMap<>();
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
        Geometry geometry = transformToEsri((PointGeometry) entity.getGeometry());
        Symbol symbol = createSymbol(entity.getSymbol());
        return new Graphic(geometry, symbol);
    }

    private Symbol createSymbol(com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol symbol) {
        return new TextSymbol(10, "Pin", Color.RED);
    }

    private Point transformToEsri(PointGeometry point) {
        Point p;
        if (point.hasAltitude()) {
            p = new Point(point.getLongitude(), point.getLatitude(), point.getAltitude());
        } else {
            p = new Point(point.getLongitude(), point.getLatitude());
        }

        return (Point) projectFromWGS84(p);
    }

    private Geometry projectFromWGS84(Geometry geometry) {
        return GeometryEngine.project(geometry, mDataSR, mMapSR);
    }
}
