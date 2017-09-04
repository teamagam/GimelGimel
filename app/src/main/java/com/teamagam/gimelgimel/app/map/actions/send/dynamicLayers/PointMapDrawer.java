package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.List;

class PointMapDrawer extends AbsMapDrawer {

  PointMapDrawer(MapEntityFactory mapEntityFactory,
      MapDrawer mapDrawer,
      OnEditingStartListener listener) {
    super(mapEntityFactory, mapDrawer, listener);
  }

  @Override
  public void setupSymbologyPanel(EditDynamicLayerViewModel.SymbologyPanelVisibilitySetter setter) {
    super.setupSymbologyPanel(setter);
    setter.showIcon();
  }

  @Override
  protected GeoEntity buildEntity(MapEntityFactory mapEntityFactory,
      List<PointGeometry> pointsHistory,
      Symbol symbol) {
    return mapEntityFactory.createPoint(getLast(pointsHistory), (PointSymbol) symbol);
  }

  private PointGeometry getLast(List<PointGeometry> pointsHistory) {
    if (pointsHistory.size() > 0) {
      return pointsHistory.get(pointsHistory.size() - 1);
    }
    throw new RuntimeException("point history is empty. Cannot get last point");
  }
}
