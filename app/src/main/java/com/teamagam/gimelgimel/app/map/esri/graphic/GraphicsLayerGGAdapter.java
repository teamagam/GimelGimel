package com.teamagam.gimelgimel.app.map.esri.graphic;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.Symbol;
import com.teamagam.gimelgimel.app.common.utils.BiMap;
import com.teamagam.gimelgimel.app.map.esri.EsriUtils;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public class GraphicsLayerGGAdapter {

  private final GraphicsLayer mGraphicsLayer;
  private final SpatialReference mDataSR;
  private final SpatialReference mMapSR;
  private final BiMap<String, Integer> mEntityIdToGraphicId;
  private final EsriSymbolCreator mSymbolCreator;

  public GraphicsLayerGGAdapter(GraphicsLayer graphicsLayer,
      SpatialReference sourceSR,
      SpatialReference mapSR,
      EsriSymbolCreator esriSymbolCreator) {
    mGraphicsLayer = graphicsLayer;
    mDataSR = sourceSR;
    mMapSR = mapSR;
    mEntityIdToGraphicId = new BiMap<>();
    mSymbolCreator = esriSymbolCreator;
  }

  public void draw(GeoEntity entity) {
    removeIfExist(entity);

    Graphic graphic = createGraphic(entity);
    int graphicId = mGraphicsLayer.addGraphic(graphic);
    mEntityIdToGraphicId.put(entity.getId(), graphicId);
  }

  public void remove(String entityId) {
    if (mEntityIdToGraphicId.containsKey(entityId)) {
      int gId = mEntityIdToGraphicId.getValue(entityId);
      mGraphicsLayer.removeGraphic(gId);
    }
  }

  public String getEntityId(int graphicId) {
    return mEntityIdToGraphicId.getKey(graphicId);
  }

  private void removeIfExist(GeoEntity entity) {
    if (mEntityIdToGraphicId.containsKey(entity.getId())) {
      remove(entity.getId());
    }
  }

  private Graphic createGraphic(GeoEntity entity) {
    Geometry geometry = EsriUtils.transformAndProject(entity.getGeometry(), mDataSR, mMapSR);
    Symbol symbol = mSymbolCreator.create(entity.getSymbol());

    return new Graphic(geometry, symbol);
  }
}