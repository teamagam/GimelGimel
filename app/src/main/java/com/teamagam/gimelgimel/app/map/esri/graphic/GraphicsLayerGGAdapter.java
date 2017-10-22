package com.teamagam.gimelgimel.app.map.esri.graphic;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.Symbol;
import com.teamagam.gimelgimel.app.common.utils.BiMap;
import com.teamagam.gimelgimel.app.map.esri.EsriUtils;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public class GraphicsLayerGGAdapter {

  private final GraphicsOverlay mGraphicsOverlay;
  private final SpatialReference mDataSR;
  private final SpatialReference mMapSR;
  private final BiMap<String, Graphic> mEntityIdToGraphic;
  private final EsriSymbolCreator mSymbolCreator;

  public GraphicsLayerGGAdapter(GraphicsOverlay graphicsOverlay,
      SpatialReference sourceSR,
      SpatialReference mapSR,
      EsriSymbolCreator esriSymbolCreator) {
    mGraphicsOverlay = graphicsOverlay;
    mDataSR = sourceSR;
    mMapSR = mapSR;
    mEntityIdToGraphic = new BiMap<>();
    mSymbolCreator = esriSymbolCreator;
  }

  public void draw(GeoEntity entity) {
    removeIfExist(entity);
    Graphic graphic = createGraphic(entity);
    mGraphicsOverlay.getGraphics().add(graphic);
    mEntityIdToGraphic.put(entity.getId(), graphic);
  }

  public void remove(String entityId) {
    if (mEntityIdToGraphic.containsKey(entityId)) {
      Graphic graphic = mEntityIdToGraphic.getValue(entityId);
      mGraphicsOverlay.getGraphics().remove(graphic);
    }
  }

  public String getEntityId(Graphic graphic) {
    return mEntityIdToGraphic.getKey(graphic);
  }

  private void removeIfExist(GeoEntity entity) {
    if (mEntityIdToGraphic.containsKey(entity.getId())) {
      remove(entity.getId());
    }
  }

  private Graphic createGraphic(GeoEntity entity) {
    Geometry geometry = EsriUtils.transformAndProject(entity.getGeometry(), mDataSR, mMapSR);
    Symbol symbol = mSymbolCreator.create(entity.getSymbol());

    return new Graphic(geometry, symbol);
  }
}