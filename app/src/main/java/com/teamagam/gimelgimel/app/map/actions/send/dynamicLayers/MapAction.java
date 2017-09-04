package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.Collection;

interface MapAction {

  void setupSymbologyPanel(EditDynamicLayerViewModel.SymbologyPanelVisibilitySetter setter);

  void setupAction(GGMapView ggMapView, Symbol symbol);

  void stop();

  void start();

  void updateSymbol(Symbol symbol);

  Collection<GeoEntity> getEntities();
}
