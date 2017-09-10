package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.Collection;

class DetailsAction implements MapAction {
  @Override
  public void setupSymbologyPanel(EditDynamicLayerViewModel.SymbologyPanelVisibilitySetter setter) {
    setter.showDetailsPanel();
  }

  @Override
  public void setupAction(GGMapView ggMapView, Symbol symbol) {

  }

  @Override
  public void stop() {

  }

  @Override
  public void start() {

  }

  @Override
  public void updateSymbol(Symbol symbol) {

  }

  @Override
  public Collection<GeoEntity> getEntities() {
    return null;
  }
}
