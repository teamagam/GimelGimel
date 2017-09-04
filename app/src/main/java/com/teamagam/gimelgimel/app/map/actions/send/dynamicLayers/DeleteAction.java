package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.Collection;
import java.util.Collections;

class DeleteAction extends AbsMapAction {

  private final DynamicLayerEntityDeleteListener mDynamicLayerEntityDeleteListener;
  private GGMapView mGgMapView;

  DeleteAction(DynamicLayerEntityDeleteListener dynamicLayerEntityDeleteListener) {
    mDynamicLayerEntityDeleteListener = dynamicLayerEntityDeleteListener;
  }

  @Override
  public void setupAction(GGMapView ggMapView, Symbol symbol) {
    mGgMapView = ggMapView;
    mGgMapView.setOnEntityClickedListener(mDynamicLayerEntityDeleteListener);
  }

  @Override
  public void stop() {
    mDynamicLayerEntityDeleteListener.stopDynamicLayerEntitiesSync();
    if (mGgMapView != null) {
      mGgMapView.setOnEntityClickedListener(null);
    }
  }

  @Override
  public void start() {
    mDynamicLayerEntityDeleteListener.startDynamicLayerEntitiesSync();
  }

  @Override
  public void updateSymbol(Symbol symbol) {

  }

  @Override
  public Collection<GeoEntity> getEntities() {
    return Collections.emptyList();
  }
}
