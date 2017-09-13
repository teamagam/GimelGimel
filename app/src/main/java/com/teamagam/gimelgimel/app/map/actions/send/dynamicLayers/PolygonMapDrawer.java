package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.List;

class PolygonMapDrawer extends MultiplePointsMapDrawer {

  protected PolygonMapDrawer(MapEntityFactory mapEntityFactory,
      MapDrawer mapDrawer,
      OnEditingStartListener listener) {
    super(mapEntityFactory, mapDrawer, listener);
  }

  @Override
  public void setupSymbologyPanel(EditDynamicLayerViewModel.SymbologyPanelVisibilitySetter setter) {
    super.setupSymbologyPanel(setter);
    setter.showBorder();
    setter.showFill();
  }

  @Override
  GeoEntity createMultipointEntity(MapEntityFactory mapEntityFactory,
      List<PointGeometry> pointsHistory,
      Symbol symbol) {
    return mapEntityFactory.createPolygon(pointsHistory, (PolygonSymbol) symbol);
  }
}
