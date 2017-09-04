package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.List;

class PolylineMapDrawer extends AbsMapDrawer {

  protected PolylineMapDrawer(MapEntityFactory mapEntityFactory,
      MapDrawer mapDrawer,
      OnEditingStartListener listener) {
    super(mapEntityFactory, mapDrawer, listener);
  }

  @Override
  public void setupSymbologyPanel(EditDynamicLayerViewModel.SymbologyPanelVisibilitySetter setter) {
    super.setupSymbologyPanel(setter);
    setter.showBorder();
  }

  @Override
  protected GeoEntity buildEntity(MapEntityFactory mapEntityFactory,
      List<PointGeometry> pointsHistory,
      Symbol symbol) {
    return mapEntityFactory.createPolyline(pointsHistory, (PolylineSymbol) symbol);
  }
}
