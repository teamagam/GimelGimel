package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.teamagam.gimelgimel.app.common.utils.BiMap;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

import java.util.Arrays;

public class GraphicsLayerGGAdapter {

    private static final int SELECTION_BG_COLOR = Color.BLUE;
    private static final int SELECTION_CIRCLE_SIZE_DP = 25;

    private final Context mContext;
    private final GraphicsLayer mGraphicsLayer;
    private final SpatialReference mDataSR;
    private final SpatialReference mMapSR;
    private final BiMap<String, Integer> mEntityIdToGraphicId;

    public GraphicsLayerGGAdapter(Context context,
                                  GraphicsLayer graphicsLayer,
                                  SpatialReference sourceSR,
                                  SpatialReference mapSR) {
        mContext = context;
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

    public String getEntityId(int graphicId) {
        return mEntityIdToGraphicId.getKey(graphicId);
    }

    private Graphic createGraphic(GeoEntity entity) {
        Geometry geometry = EsriUtils.transformAndProject(
                (PointGeometry) entity.getGeometry(), mDataSR, mMapSR);
        Symbol symbol = createSymbol(entity.getSymbol());
        return new Graphic(geometry, symbol);
    }

    private Symbol createSymbol(
            com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol) {
        Symbol esriSymbol = transform(ggSymbol);
        if (ggSymbol.isSelected()) {
            return addSelectionDecoration(esriSymbol);
        }
        return esriSymbol;
    }

    private Symbol transform(
            com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol ggSymbol) {
        EsriSymbolCreationVisitor symbolVisitor = new EsriSymbolCreationVisitor(mContext);
        ggSymbol.accept(symbolVisitor);
        return symbolVisitor.getEsriSymbol();
    }

    private Symbol addSelectionDecoration(Symbol baseSymbol) {
        Symbol selectionSymbol = new SimpleMarkerSymbol(
                SELECTION_BG_COLOR,
                SELECTION_CIRCLE_SIZE_DP,
                SimpleMarkerSymbol.STYLE.CIRCLE);
        return new CompositeSymbol(Arrays.asList(selectionSymbol, baseSymbol));
    }
}