package com.teamagam.gimelgimel.app.map.esri.graphic;

import android.content.Context;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.BiMap;
import com.teamagam.gimelgimel.app.map.esri.EsriUtils;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public class GraphicsLayerGGAdapter {

    private final GraphicsLayer mGraphicsLayer;
    private final SpatialReference mDataSR;
    private final SpatialReference mMapSR;
    private final BiMap<String, Integer> mEntityIdToGraphicId;
    private final EsriSymbolCreator mSymbolCreator;

    public GraphicsLayerGGAdapter(Context context,
                                  GraphicsLayer graphicsLayer,
                                  SpatialReference sourceSR,
                                  SpatialReference mapSR) {
        mGraphicsLayer = graphicsLayer;
        mDataSR = sourceSR;
        mMapSR = mapSR;
        mEntityIdToGraphicId = new BiMap<>();
        mSymbolCreator = new EsriSymbolCreator(context);
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

    public String getEntityId(int graphicId) {
        return mEntityIdToGraphicId.getKey(graphicId);
    }

    private Graphic createGraphic(GeoEntity entity) {
        Geometry geometry = EsriUtils.transformAndProject(entity.getGeometry(), mDataSR, mMapSR);
        Symbol symbol = mSymbolCreator.create(entity.getSymbol());

        return new Graphic(geometry, symbol);
    }
}